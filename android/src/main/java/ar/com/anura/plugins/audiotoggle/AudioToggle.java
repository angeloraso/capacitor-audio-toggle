package ar.com.anura.plugins.audiotoggle;

import static android.media.AudioManager.STREAM_VOICE_CALL;

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
    private int savedStreamVolume;
    private AudioDeviceManagerInterface audioManager;
    private VolumeChangeReceiver volumeReceiver;

    AudioToggle(final AppCompatActivity activity) {
        this.activity = activity;
        this.audioManager = AudioDeviceManagerService.get(activity);
    }

    public void start() {
        this.savedStreamVolume = activity.getVolumeControlStream();

        volumeReceiver = new VolumeChangeReceiver();
        IntentFilter filter = new IntentFilter("android.media.VOLUME_CHANGED_ACTION");
        activity.getApplicationContext().registerReceiver(volumeReceiver, filter);
    }

    public void stop() {
        audioManager.reset();
        activity.setVolumeControlStream(savedStreamVolume);
        audioManager.onDestroy();
        audioManager = null;

        if (volumeReceiver != null) {
            activity.getApplicationContext().unregisterReceiver(volumeReceiver);
            volumeReceiver = null;
        }
    }

    public void setSpeakerStatusEventListener(AudioDeviceManagerListener listener) {
        audioManager.setSpeakerChangeListener(listener);
    }

    public void setVolumeChangeEventListener(VolumeChangeEventListener listener) {
        volumeChangeEventListener = listener;
    }

    public void setSpeakerOn(boolean turnOn) {
        audioManager.setSpeakerOn(turnOn);
        activity.setVolumeControlStream(STREAM_VOICE_CALL);
    }

    public void onDestroy() {
        stop();
    }
}
