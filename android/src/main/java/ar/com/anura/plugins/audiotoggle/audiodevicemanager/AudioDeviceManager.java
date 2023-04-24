package ar.com.anura.plugins.audiotoggle.audiodevicemanager;

import static android.media.AudioManager.AUDIOFOCUS_REQUEST_GRANTED;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class AudioDeviceManager {

    protected static final String TAG = "Audio Device Manager";

    protected AudioManager audioManager = null;
    private int savedMode;
    private boolean savedSpeakerphone;
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
    private AudioFocusRequest audioRequest = null;

    AudioDeviceManager(final AppCompatActivity activity) {
        if (this.audioManager == null) {
            this.audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        }

        this.audioFocusChangeListener =
            focusChange -> {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        Log.d(TAG, "Audio focus gain");
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        Log.d(TAG, "Audio focus loss");
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        Log.d(TAG, "Audio focus loss transient");
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        Log.d(TAG, "Audio focus loss transient can duck");
                        break;
                }
            };
    }

    public void setSpeakerOn(boolean speakerOn) {}

    public void setAudioFocus(int sleep) {
        if (audioRequest == null) {
            try {
                Thread.sleep(sleep);
                AudioAttributes playbackAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
                AudioFocusRequest.Builder focusRequestBuilder = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                    .setAudioAttributes(playbackAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener(audioFocusChangeListener);
                focusRequestBuilder.setWillPauseWhenDucked(true);
                this.audioRequest = focusRequestBuilder.build();

                int res = audioManager.requestAudioFocus(audioRequest);
                if (res == AUDIOFOCUS_REQUEST_GRANTED) {
                    savedSpeakerphone = audioManager.isSpeakerphoneOn();
                    savedMode = audioManager.getMode();
                    audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                    audioManager.setMicrophoneMute(false);
                }
            } catch (InterruptedException e) {
                Log.d(TAG, "Set focus error");
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        audioManager.setMode(savedMode);
        audioManager.setSpeakerphoneOn(savedSpeakerphone);
        if (audioRequest != null) {
            int res = audioManager.abandonAudioFocusRequest(audioRequest);
            if (res == AUDIOFOCUS_REQUEST_GRANTED) {
                audioRequest = null;
            }
        }
    }
}
