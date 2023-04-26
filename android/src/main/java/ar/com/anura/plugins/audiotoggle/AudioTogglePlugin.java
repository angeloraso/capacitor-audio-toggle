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
        audioToggle.setAudioToggleEventListener(this::onAudioToggleEvent);
    }

    private void onAudioToggleEvent(boolean speakerOn) {
        JSObject res = new JSObject();

        res.put("status", speakerOn);

        bridge.triggerWindowJSEvent("speakerOn");
        notifyListeners("speakerOn", res);
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

    @PluginMethod
    public void reset(PluginCall call) {
        if (getActivity().isFinishing()) {
            call.reject("Audio toggle plugin error: App is finishing");
            return;
        }

        audioToggle.reset();

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
