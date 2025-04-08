package ar.com.anura.plugins.audiotoggle.audiodevicemanager;

import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Timer;
import java.util.TimerTask;

public class AudioDeviceManager29 extends AudioDeviceManager implements AudioDeviceManagerInterface {

    final long delay = 0;
    final long interval = 500;
    boolean turnOn = false;
    Timer timer;
    TimerTask task;

    AudioDeviceManager29(final AppCompatActivity activity) {
        super(activity);
    }

    public void start(AudioDeviceManagerListener listener) {
        super.start(listener);
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

            timer.schedule(task, delay, interval);
        }
    }

    public void stop() {
        stopTimer();
        super.stop();
        notifySpeakerStatus();
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
