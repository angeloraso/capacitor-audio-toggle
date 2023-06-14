package ar.com.anura.plugins.audiotoggle.audiodevicemanager;

import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.S)
public class AudioDeviceManager31
    extends AudioDeviceManager
    implements AudioDeviceManagerInterface, AudioManager.OnCommunicationDeviceChangedListener, AudioManager.OnModeChangedListener {

    private boolean first = true;
    private boolean notified = false;

    AudioDeviceManager31(final AppCompatActivity activity) {
        super(activity);
        registerAudioDeviceCallbacks(this::onAudioDevicesAdded, this::onAudioDevicesRemoved);
        audioManager.addOnCommunicationDeviceChangedListener(activity.getMainExecutor(), this);
        audioManager.addOnModeChangedListener(activity.getMainExecutor(), this);
    }

    @Override
    public void setSpeakerOn(boolean speakerOn) {
        notified = false;
        Thread checkNotify = new Thread(
            () -> {
                try {
                    Thread.sleep(3000);
                    if (!notified) {
                        notifySpeakerStatus();
                    }
                } catch (InterruptedException v) {
                    System.out.println(v);
                    notifySpeakerStatus();
                }
            }
        );
        checkNotify.start();

        if (isBluetoothConnected()) {
            super.setAudioFocus(1300);
        } else {
            super.setAudioFocus(0);
        }

        if (first) {
            first = false;

            if (isWiredConnected() || isBluetoothConnected()) {
                setSpeakerOn();
            }
        }

        boolean success;

        if (!speakerOn) {
            if (isWiredConnected() || isBluetoothConnected()) {
                audioManager.clearCommunicationDevice();
            } else {
                AudioDeviceInfo earpieceDevice = getAudioDevice(AudioDeviceInfo.TYPE_BUILTIN_EARPIECE);
                if (earpieceDevice != null) {
                    success = audioManager.setCommunicationDevice(earpieceDevice);
                    if (!success) {
                        Log.d(TAG, "Earpiece error");
                        notifySpeakerStatus();
                    }
                }
            }
        } else {
            AudioDeviceInfo deviceInfo = audioManager.getCommunicationDevice();
            if (deviceInfo != null && deviceInfo.getType() == AudioDeviceInfo.TYPE_BUILTIN_EARPIECE) {
                audioManager.clearCommunicationDevice();
                audioManager.setMode(AudioManager.MODE_NORMAL);
            } else if (isWiredConnected() || isBluetoothConnected()) {
                setSpeakerOn();
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        first = true;
        audioManager.clearCommunicationDevice();
        notifySpeakerStatus();
    }

    public void setSpeakerChangeListener(AudioDeviceManagerListener speakerChangeListener) {
        super.setSpeakerChangeListener(speakerChangeListener);
    }

    public void onDestroy() {
        super.onDestroy();
        audioManager.removeOnCommunicationDeviceChangedListener(this);
        audioManager.removeOnModeChangedListener(this);
    }

    private void setSpeakerOn() {
        audioManager.setMode(AudioManager.MODE_NORMAL);
        AudioDeviceInfo speakerDevice = getAudioDevice(AudioDeviceInfo.TYPE_BUILTIN_SPEAKER);
        if (speakerDevice != null) {
            boolean success = audioManager.setCommunicationDevice(speakerDevice);
            if (!success) {
                Log.d(TAG, "Speaker error");
                notifySpeakerStatus();
            }
        }
    }

    @Override
    public void onCommunicationDeviceChanged(@Nullable AudioDeviceInfo audioDeviceInfo) {
        if (audioDeviceInfo != null) {
            Log.d(TAG, "Device changed: " + audioDeviceInfo.getType());
        }
        showCurrentAudioDevice();
        notifySpeakerStatus();
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
    public void onModeChanged(int iMode) {
        showMode(iMode);
        notifySpeakerStatus();
    }

    private void showMode(int iMode) {
        String strMode = "";
        switch (iMode) {
            case AudioManager.MODE_NORMAL:
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

    private void onAudioDevicesAdded(AudioDeviceInfo[] addedDevices) {
        notifySpeakerStatus();
    }

    private void onAudioDevicesRemoved(AudioDeviceInfo[] removedDevices) {
        notifySpeakerStatus();
    }

    private boolean isBluetoothConnected() {
        AudioDeviceInfo bluetoothScoDevice = getAudioDevice(AudioDeviceInfo.TYPE_BLUETOOTH_SCO);
        AudioDeviceInfo bluetoothA2DPDevice = getAudioDevice(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP);
        return (bluetoothScoDevice != null || bluetoothA2DPDevice != null);
    }

    private boolean isWiredConnected() {
        AudioDeviceInfo wiredHeadphonesDevice = getAudioDevice(AudioDeviceInfo.TYPE_WIRED_HEADPHONES);
        AudioDeviceInfo wiredHeadsetDevice = getAudioDevice(AudioDeviceInfo.TYPE_WIRED_HEADSET);
        return (wiredHeadphonesDevice != null || wiredHeadsetDevice != null);
    }

    private void notifySpeakerStatus() {
        boolean status = false;
        AudioDeviceInfo communicationDevice = audioManager.getCommunicationDevice();
        if (communicationDevice != null) {
            status = communicationDevice.getType() == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER;
        }
        Log.d(TAG, "Speaker on: " + status);
        speakerChangeListener.speakerOn(status);
        notified = true;
    }
}
