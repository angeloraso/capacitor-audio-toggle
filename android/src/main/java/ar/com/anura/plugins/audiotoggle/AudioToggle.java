package ar.com.anura.plugins.audiotoggle;

import androidx.appcompat.app.AppCompatActivity;
import ar.com.anura.plugins.audiotoggle.audiodevicemanager.AudioDeviceManagerInterface;
import ar.com.anura.plugins.audiotoggle.audiodevicemanager.AudioDeviceManagerListener;
import ar.com.anura.plugins.audiotoggle.audiodevicemanager.AudioDeviceManagerService;

public class AudioToggle {

    private AudioDeviceManagerInterface audioManager;

    AudioToggle(final AppCompatActivity activity) {
        this.audioManager = AudioDeviceManagerService.get(activity);
    }

    public void setAudioToggleEventListener(AudioDeviceManagerListener listener) {
        audioManager.setSpeakerChangeListener(listener);
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
