package ar.com.anura.plugins.audiotoggle.audiodevicemanager;

import android.media.AudioDeviceInfo;

public interface AudioDevicesChanged {
    void on(AudioDeviceInfo[] devices);
}
