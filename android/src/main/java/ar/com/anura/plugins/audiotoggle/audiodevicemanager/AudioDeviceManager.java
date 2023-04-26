package ar.com.anura.plugins.audiotoggle.audiodevicemanager;

import static android.media.AudioManager.AUDIOFOCUS_REQUEST_GRANTED;

import android.content.Context;
import android.media.AudioDeviceCallback;
import android.media.AudioDeviceInfo;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public abstract class AudioDeviceManager {

    protected static final String TAG = "Audio Device Manager";
    protected AudioManager audioManager = null;
    private int savedMode;
    private boolean savedMicrophone;
    private AudioDeviceCallback audioDeviceCallback;

    public AudioDeviceManagerListener speakerChangeListener;

    private AudioFocusRequest audioRequest = null;

    AudioDeviceManager(final AppCompatActivity activity) {
        if (this.audioManager == null) {
            this.audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        }
    }

    protected void registerAudioDeviceCallbacks(AudioDevicesChanged onAudioDevicesAdded, AudioDevicesChanged onAudioDevicesRemoved) {
        this.audioDeviceCallback =
            new AudioDeviceCallback() {
                @Override
                public void onAudioDevicesAdded(AudioDeviceInfo[] addedDevices) {
                    Log.d(TAG, "Added devices: " + addedDevices);
                    onAudioDevicesAdded.on(addedDevices);
                }

                @Override
                public void onAudioDevicesRemoved(AudioDeviceInfo[] removedDevices) {
                    Log.d(TAG, "Removed devices: " + removedDevices);
                    onAudioDevicesRemoved.on(removedDevices);
                }
            };
        audioManager.registerAudioDeviceCallback(this.audioDeviceCallback, null);
    }

    protected void setAudioFocus(int sleep) {
        if (audioRequest == null) {
            try {
                Thread.sleep(sleep);
                this.audioRequest = AudioDeviceFocusRequest.get();

                int res = audioManager.requestAudioFocus(audioRequest);
                if (res == AUDIOFOCUS_REQUEST_GRANTED) {
                    savedMode = audioManager.getMode();
                    savedMicrophone = audioManager.isMicrophoneMute();
                    audioManager.setMicrophoneMute(false);
                    audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                }
            } catch (InterruptedException e) {
                Log.d(TAG, "Set focus error");
                e.printStackTrace();
            }
        }
    }

    protected void reset() {
        audioManager.setMode(savedMode);
        audioManager.setMicrophoneMute(savedMicrophone);
        if (audioRequest != null) {
            int res = audioManager.abandonAudioFocusRequest(audioRequest);
            if (res == AUDIOFOCUS_REQUEST_GRANTED) {
                audioRequest = null;
            }
        }
    }

    protected void setSpeakerChangeListener(AudioDeviceManagerListener speakerChangeListener) {
        this.speakerChangeListener = speakerChangeListener;
    }

    protected void onDestroy() {
        reset();
        audioManager.unregisterAudioDeviceCallback(audioDeviceCallback);
    }
}
