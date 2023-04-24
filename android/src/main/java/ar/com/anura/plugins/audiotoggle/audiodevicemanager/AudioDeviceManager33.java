package ar.com.anura.plugins.audiotoggle.audiodevicemanager;

import android.media.AudioDeviceCallback;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class AudioDeviceManager33
    extends AudioDeviceManager
    implements AudioManager.OnCommunicationDeviceChangedListener, AudioManager.OnModeChangedListener {

    List<AudioDeviceInfo> devices = new ArrayList<>();

    AudioDeviceManager33(final AppCompatActivity activity) {
        super(activity);
        audioManager.addOnCommunicationDeviceChangedListener(activity.getMainExecutor(), this);
        audioManager.addOnModeChangedListener(activity.getMainExecutor(), this);
        final AudioDeviceCallback audioDeviceCallback = new AudioDeviceCallback() {
            @Override
            public void onAudioDevicesAdded(AudioDeviceInfo[] addedDevices) {
                devices.addAll(Arrays.asList(addedDevices));
            }

            @Override
            public void onAudioDevicesRemoved(AudioDeviceInfo[] removedDevices) {
                for (AudioDeviceInfo device : removedDevices) {
                    while (devices.contains(device)) {
                        devices.remove(device);
                    }
                }
            }
        };

        audioManager.registerAudioDeviceCallback(audioDeviceCallback, null);
    }

    public void setSpeakerOn(boolean speakerOn) {
        super.setAudioFocus(1300);
        boolean success = false;

        if (!speakerOn) {
            boolean thereIsBluetooth = false;
            for (AudioDeviceInfo device : devices) {
                if (device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_SCO) {
                    thereIsBluetooth = true;
                    break;
                }
            }

            if (thereIsBluetooth) {
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                AudioDeviceInfo bluetoothDevice = getAudioDevice(AudioDeviceInfo.TYPE_BLUETOOTH_SCO);
                if (bluetoothDevice != null) {
                    success = audioManager.setCommunicationDevice(bluetoothDevice);
                    if (success) {
                        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                    } else {
                        Log.d(TAG, "Bluetooth error");
                    }
                }
                return;
            }

            boolean thereIsWired = false;
            for (AudioDeviceInfo device : devices) {
                if (device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES || device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET) {
                    thereIsWired = true;
                    break;
                }
            }

            if (thereIsWired) {
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                AudioDeviceInfo wiredHeadphonesDevice = getAudioDevice(AudioDeviceInfo.TYPE_WIRED_HEADPHONES);
                AudioDeviceInfo wiredHeadsetDevice = getAudioDevice(AudioDeviceInfo.TYPE_WIRED_HEADSET);
                if (wiredHeadphonesDevice != null) {
                    success = audioManager.setCommunicationDevice(wiredHeadphonesDevice);
                } else if (wiredHeadsetDevice != null) {
                    success = audioManager.setCommunicationDevice(wiredHeadsetDevice);
                }

                if (success) {
                    audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                } else {
                    Log.d(TAG, "Wired error");
                }
                return;
            }

            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            AudioDeviceInfo earpieceDevice = getAudioDevice(AudioDeviceInfo.TYPE_BUILTIN_EARPIECE);
            if (earpieceDevice != null) {
                success = audioManager.setCommunicationDevice(earpieceDevice);
                if (success) {
                    audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                } else {
                    Log.d(TAG, "Earpiece error");
                }
            }
        } else {
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            AudioDeviceInfo speakerphoneDevice = getAudioDevice(AudioDeviceInfo.TYPE_BUILTIN_SPEAKER);
            if (speakerphoneDevice != null) {
                success = audioManager.setCommunicationDevice(speakerphoneDevice);
                if (success) {
                    audioManager.setSpeakerphoneOn(true);
                    audioManager.setMode(AudioManager.MODE_NORMAL);
                } else {
                    Log.d(TAG, "Speaker error");
                }
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        audioManager.clearCommunicationDevice();
    }

    private void showCurrentAudioDevice() {
        AudioDeviceInfo currentDevice = audioManager.getCommunicationDevice();
        if (currentDevice != null) {
            Log.d(
                TAG,
                "Current device: " +
                currentDevice.getId() +
                "," +
                currentDevice.getType() +
                "," +
                currentDevice.getProductName() +
                "," +
                currentDevice.getAddress() +
                "," +
                currentDevice.isSink()
            );
        }
    }

    @Override
    public void onCommunicationDeviceChanged(@Nullable AudioDeviceInfo audioDeviceInfo) {
        Log.d(TAG, "Device changed: " + audioDeviceInfo.getType());
        showCurrentAudioDevice();
    }

    @Override
    public void onModeChanged(int iMode) {
        showMode(iMode);
    }

    private void showMode(int iMode) {
        String strMode = "";
        switch (iMode) {
            case AudioManager.MODE_NORMAL:
                audioManager.clearCommunicationDevice();
                strMode = "NORMAL";
                break;
            case AudioManager.MODE_IN_COMMUNICATION:
                strMode = "IN_COMMUNICATION";
                break;
            default:
                strMode = "Other: " + iMode;
                break;
        }
        Log.d(TAG, "Mode changed: " + strMode);
    }

    private AudioDeviceInfo getAudioDevice(Integer type) {
        List<AudioDeviceInfo> devices = audioManager.getAvailableCommunicationDevices();
        for (AudioDeviceInfo device : devices) {
            if (type == device.getType()) return device;
        }

        return null;
    }
}
