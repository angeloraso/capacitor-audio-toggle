package ar.com.anura.plugins.audiotoggle;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.S)
public class AudioToggle implements AudioManager.OnCommunicationDeviceChangedListener, AudioManager.OnModeChangedListener {

    private static final String TAG = "Audio toggle";
    private Context context;
    private AppCompatActivity activity;
    private AudioManager audioManager = null;

    AudioToggle(final AppCompatActivity activity, final Context context) {
        this.activity = activity;
        this.context = context;
        if (this.audioManager == null) {
            this.audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
            audioManager.addOnCommunicationDeviceChangedListener(activity.getMainExecutor(), this);
            audioManager.addOnModeChangedListener(activity.getMainExecutor(), this);
        }
    }

    public void setAudioDevice(String device) {
        switch (device) {
            case "earpiece":
                setAudioDevice(AudioDeviceInfo.TYPE_BUILTIN_EARPIECE);
                break;
            case "speaker":
                setAudioDevice(AudioDeviceInfo.TYPE_BUILTIN_SPEAKER);
                break;
            default:
                break;
        }
    }

    public void reset() {
        audioManager.clearCommunicationDevice();
        audioManager.setMode(AudioManager.MODE_NORMAL);
    }

    private void setAudioDevice(int iTypeId) {
        Log.d(TAG, "setAudioDevice: " + iTypeId);
        if (audioManager.getMode() != AudioManager.MODE_IN_COMMUNICATION) {
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        }

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

        AudioDeviceInfo targetDevice = null;
        List<AudioDeviceInfo> devices = audioManager.getAvailableCommunicationDevices();
        for (AudioDeviceInfo device : devices) {
            Log.d(
                TAG,
                "Device: " +
                device.getId() +
                "," +
                device.getType() +
                "," +
                device.getProductName() +
                "," +
                device.getAddress() +
                "," +
                device.isSink()
            );
        }

        for (AudioDeviceInfo device : devices) {
            if (device.getType() == iTypeId) {
                targetDevice = device;
                break;
            }
        }
        if (targetDevice != null) {
            Log.d(
                TAG,
                "Target Device: " +
                targetDevice.getId() +
                "," +
                targetDevice.getType() +
                "," +
                targetDevice.getProductName() +
                "," +
                targetDevice.getAddress()
            );
            // Turn speakerphone ON.
            boolean result = audioManager.setCommunicationDevice(targetDevice);
            Log.d(TAG, "result: " + result);
            if (result) {
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                //showCurrentAudioDevice();
            } else {
                Log.e(TAG, "Could no set device");
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
}
