package ar.com.anura.plugins.audiotoggle;

import androidx.appcompat.app.AppCompatActivity;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "AudioToggle")
public class AudioTogglePlugin extends Plugin {

    private AudioToggle audioToggle;

    public void load() {
        AppCompatActivity activity = getActivity();
        audioToggle = new AudioToggle(activity);
    }

    private void onSpeakerStatusEventListener(boolean speakerOn) {
        JSObject res = new JSObject();

        res.put("status", speakerOn);

        bridge.triggerWindowJSEvent("speakerOn");
        notifyListeners("speakerOn", res);
    }

    private void onValueChangeEventListener(Integer streamType, Integer newVolume, Integer oldVolume) {
        JSObject res = new JSObject();

        res.put("streamType", streamType);
        res.put("newVolume", newVolume);
        res.put("oldVolume", oldVolume);

        bridge.triggerWindowJSEvent("volumeChange");
        notifyListeners("volumeChange", res);
    }

    @PluginMethod
    public void start(PluginCall call) {
        if (getActivity().isFinishing()) {
            call.reject("Audio toggle plugin error: App is finishing");
            return;
        }

        audioToggle.start();
        audioToggle.setSpeakerStatusEventListener(this::onSpeakerStatusEventListener);
        audioToggle.setVolumeChangeEventListener(this::onValueChangeEventListener);

        call.resolve();
    }

    @PluginMethod
    public void stop(PluginCall call) {
        if (getActivity().isFinishing()) {
            call.reject("Audio toggle plugin error: App is finishing");
            return;
        }

        audioToggle.stop();
        call.resolve();
    }

    @PluginMethod
    public void setSpeakerOn(PluginCall call) {
        if (getActivity().isFinishing()) {
            call.reject("Audio toggle plugin error: App is finishing");
            return;
        }

        Boolean speakerOn = call.getBoolean("speakerOn", false);
        audioToggle.setSpeakerOn(speakerOn);

        call.resolve();
    }

    /**
     * Called when the activity will be destroyed.
     */
    @Override
    public void handleOnDestroy() {
        audioToggle.onDestroy();
    }
}
