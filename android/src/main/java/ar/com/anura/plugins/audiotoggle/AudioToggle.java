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

    private boolean first = true;

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
        if (first) {
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            first = false;
        }
        switch (device) {
            case "earpiece":
                AudioDeviceInfo earpieceDevice = getAudioDevice(AudioDeviceInfo.TYPE_BUILTIN_EARPIECE);
                if (earpieceDevice != null) {
                    boolean success = audioManager.setCommunicationDevice(earpieceDevice);
                    if (!success) {
                        Log.d(TAG, "Earpiece error");
                    }
                }
                break;
            case "speaker":
                audioManager.clearCommunicationDevice();
                audioManager.setMode(AudioManager.MODE_NORMAL);
                break;
            default:
                break;
        }
    }

    public void reset() {
        first = true;
        audioManager.clearCommunicationDevice();
        audioManager.setMode(AudioManager.MODE_NORMAL);
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
