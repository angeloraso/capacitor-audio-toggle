package ar.com.anura.plugins.audiotoggle.audiodevicemanager;

import android.media.AudioManager;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class AudioDeviceManager29 extends AudioDeviceManager {

    AudioDeviceManager29(final AppCompatActivity activity) {
        super(activity);
    }

    public void setSpeakerOn(boolean speakerOn) {
        super.setAudioFocus(1000);
        audioManager.setSpeakerphoneOn(speakerOn);
        audioManager.setMode(AudioManager.MODE_NORMAL);
    }

    @Override
    public void reset() {
        super.reset();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
