package ar.com.anura.plugins.audiotoggle;

import androidx.appcompat.app.AppCompatActivity;
import ar.com.anura.plugins.audiotoggle.audiodevicemanager.AudioDeviceManager;
import ar.com.anura.plugins.audiotoggle.audiodevicemanager.AudioManagerService;

public class AudioToggle {

    private static final String TAG = "Audio toggle";

    private AudioDeviceManager audioManager;

    AudioToggle(final AppCompatActivity activity) {
        this.audioManager = AudioManagerService.get(activity);
    }

    public void setSpeakerOn(boolean turnOn) {
        audioManager.setSpeakerOn(turnOn);
    }

    public void reset() {
        audioManager.reset();
    }

    public void onDestroy() {
        audioManager.onDestroy();
    }
}
