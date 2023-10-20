package ar.com.anura.plugins.audiotoggle.audiodevicemanager;

import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class AudioDeviceManager29 extends AudioDeviceManager implements AudioDeviceManagerInterface {

    final long delay = 0;
    final long interval = 500;
    boolean turnOn = false;
    Timer timer;
    TimerTask task;

    AudioDeviceManager29(final AppCompatActivity activity) {
        super(activity);
        registerAudioDeviceCallbacks(this::onAudioDevicesAdded, this::onAudioDevicesRemoved);
    }

    public void setSpeakerOn(boolean speakerOn) {
        this.turnOn = speakerOn;
        if (timer == null) {
            timer = new Timer();
            task =
                new TimerTask() {
                    @Override
                    public void run() {
                        setAudioFocus(1000);
                        audioManager.setSpeakerphoneOn(turnOn);
                        audioManager.setMode(AudioManager.MODE_NORMAL);
                        notifySpeakerStatus();
                    }
                };

            timer.scheduleAtFixedRate(task, delay, interval);
        }
    }

    public void reset() {
        stopTimer();
        super.reset();
        notifySpeakerStatus();
    }

    public void onDestroy() {
        stopTimer();
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

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
