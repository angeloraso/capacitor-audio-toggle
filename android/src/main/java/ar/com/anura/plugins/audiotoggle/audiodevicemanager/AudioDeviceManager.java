package ar.com.anura.plugins.audiotoggle.audiodevicemanager;

import static android.media.AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
import static android.media.AudioManager.STREAM_VOICE_CALL;

import android.content.Context;
import android.media.AudioDeviceCallback;
import android.media.AudioDeviceInfo;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public abstract class AudioDeviceManager {

    protected static final String TAG = "Audio Device Manager";
    protected AudioManager audioManager = null;
    protected AppCompatActivity activity = null;
    private int savedMode;
    private boolean savedMicrophone;
    private AudioDeviceCallback audioDeviceCallback;

    public AudioDeviceManagerListener speakerChangeListener;

    private AudioFocusRequest audioRequest = null;

    private int savedStreamVolume;

    AudioDeviceManager(final AppCompatActivity activity) {
        this.activity = activity;
        if (this.audioManager == null) {
            this.audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        }
    }

    protected void start(AudioDeviceManagerListener speakerChangeListener) {
        this.speakerChangeListener = speakerChangeListener;

        savedStreamVolume = activity.getVolumeControlStream();
        activity.setVolumeControlStream(STREAM_VOICE_CALL);

        savedMode = audioManager.getMode();
        savedMicrophone = audioManager.isMicrophoneMute();
    }

    protected void registerAudioDeviceCallbacks(AudioDevicesChanged onAudioDevicesAdded, AudioDevicesChanged onAudioDevicesRemoved) {
        this.audioDeviceCallback =
            new AudioDeviceCallback() {
                @Override
                public void onAudioDevicesAdded(AudioDeviceInfo[] addedDevices) {
                    Log.d(TAG, "Added devices: " + Arrays.toString(addedDevices));
                    onAudioDevicesAdded.on(addedDevices);
                }

                @Override
                public void onAudioDevicesRemoved(AudioDeviceInfo[] removedDevices) {
                    Log.d(TAG, "Removed devices: " + Arrays.toString(removedDevices));
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
                    audioManager.setMicrophoneMute(false);
                    audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                }
            } catch (InterruptedException e) {
                Log.d(TAG, "Set focus error");
            }
        }
    }

    protected void stop() {
        if (audioRequest != null) {
            int res = audioManager.abandonAudioFocusRequest(audioRequest);
            if (res == AUDIOFOCUS_REQUEST_GRANTED) {
                audioRequest = null;
            }
        }

        audioManager.unregisterAudioDeviceCallback(audioDeviceCallback);
        audioManager.setMode(savedMode);
        audioManager.setMicrophoneMute(savedMicrophone);
        activity.setVolumeControlStream(savedStreamVolume);
    }

}
