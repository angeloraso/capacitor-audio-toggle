package ar.com.anura.plugins.audiotoggle.audiodevicemanager;

public interface AudioDeviceManagerInterface {
    void setSpeakerOn(boolean status);

    void reset();

    void setSpeakerChangeListener(AudioDeviceManagerListener speakerChangeListener);

    void onDestroy();
}
