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
    implements AudioManager.OnCommunicationDeviceChangedListener, AudioManager.OnModeChangedListener {

    private boolean first = true;

    AudioDeviceManager31(final AppCompatActivity activity) {
        super(activity);
        audioManager.addOnCommunicationDeviceChangedListener(activity.getMainExecutor(), this);
        audioManager.addOnModeChangedListener(activity.getMainExecutor(), this);
    }

    @Override
    public void setSpeakerOn(boolean speakerOn) {
        super.setAudioFocus(0);

        if (first) {
            first = false;
            if (isBluetoothConnected() || isWiredConnected()) {
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
                    }
                }
            }
        } else {
            AudioDeviceInfo deviceInfo = audioManager.getCommunicationDevice();
            if (deviceInfo.getType() == AudioDeviceInfo.TYPE_BUILTIN_EARPIECE) {
                audioManager.clearCommunicationDevice();
                audioManager.setMode(AudioManager.MODE_NORMAL);
            } else if (
                deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET ||
                deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES ||
                deviceInfo.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_SCO
            ) {
                setSpeakerOn();
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        first = true;
        audioManager.clearCommunicationDevice();
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
            if (success) {
                audioManager.setSpeakerphoneOn(true);
            } else {
                Log.d(TAG, "Speaker error");
            }
        }
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
