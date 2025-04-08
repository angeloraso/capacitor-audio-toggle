package ar.com.anura.plugins.audiotoggle.audiodevicemanager;

public interface AudioDeviceManagerInterface {
    void setSpeakerOn(boolean status);

    void start(AudioDeviceManagerListener speakerChangeListener);

    void stop();
}
