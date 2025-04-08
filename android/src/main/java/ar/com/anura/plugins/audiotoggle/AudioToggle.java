package ar.com.anura.plugins.audiotoggle;

import android.content.IntentFilter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ar.com.anura.plugins.audiotoggle.audiodevicemanager.AudioDeviceManagerInterface;
import ar.com.anura.plugins.audiotoggle.audiodevicemanager.AudioDeviceManagerListener;
import ar.com.anura.plugins.audiotoggle.audiodevicemanager.AudioDeviceManagerService;

public class AudioToggle {

    interface VolumeChangeEventListener {
        void onVolumeChangeEvent(Integer streamType, Integer newVolume, Integer oldVolume);
    }

    @Nullable
    static public VolumeChangeEventListener volumeChangeEventListener;

    final AppCompatActivity activity;
    private AudioDeviceManagerInterface audioManager;
    private VolumeChangeReceiver volumeReceiver;

    AudioToggle(final AppCompatActivity activity) {
        this.activity = activity;
    }

    public void start(AudioDeviceManagerListener audioDeviceManagerListener, VolumeChangeEventListener volumeChangeEventListener) {
        this.audioManager = AudioDeviceManagerService.get(activity);
        this.audioManager.start(audioDeviceManagerListener);

        volumeReceiver = new VolumeChangeReceiver();
        IntentFilter filter = new IntentFilter("android.media.VOLUME_CHANGED_ACTION");
        activity.getApplicationContext().registerReceiver(volumeReceiver, filter);
        setVolumeChangeEventListener(volumeChangeEventListener);
    }

    public void stop() {
        if (audioManager != null) {
            audioManager.stop();
            audioManager = null;
        }

        if (volumeReceiver != null) {
            activity.getApplicationContext().unregisterReceiver(volumeReceiver);
            volumeReceiver = null;
        }
    }

    private void setVolumeChangeEventListener(VolumeChangeEventListener listener) {
        volumeChangeEventListener = listener;
    }

    public void setSpeakerOn(boolean turnOn) {
        if (audioManager != null) {
            audioManager.setSpeakerOn(turnOn);
        }
    }

    public void onDestroy() {
        stop();
    }
}
