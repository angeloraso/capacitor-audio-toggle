package ar.com.anura.plugins.audiotoggle.audiodevicemanager;

import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class AudioDeviceManager29 extends AudioDeviceManager implements AudioDeviceManagerInterface {

    AudioDeviceManager29(final AppCompatActivity activity) {
        super(activity);
        registerAudioDeviceCallbacks(this::onAudioDevicesAdded, this::onAudioDevicesRemoved);
    }

    public void setSpeakerOn(boolean speakerOn) {
        super.setAudioFocus(1000);
        audioManager.setSpeakerphoneOn(speakerOn);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        notifySpeakerStatus();
    }

    public void reset() {
        super.reset();
        notifySpeakerStatus();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void setSpeakerChangeListener(AudioDeviceManagerListener speakerChangeListener) {
        super.setSpeakerChangeListener(speakerChangeListener);
    }

    private void onAudioDevicesAdded(AudioDeviceInfo[] addedDevices) {
        notifySpeakerStatus();
    }

    private void onAudioDevicesRemoved(AudioDeviceInfo[] removedDevices) {
        notifySpeakerStatus();
    }

    private void notifySpeakerStatus() {
        speakerChangeListener.speakerOn(audioManager.isSpeakerphoneOn());
    }
}
