package ar.com.anura.plugins.audiotoggle;

import androidx.appcompat.app.AppCompatActivity;
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

    @PluginMethod
    public void setSpeakerOn(PluginCall call) {
        if (getActivity().isFinishing()) {
            call.reject("Audio toggle plugin error: App is finishing");
            return;
        }

        String speakerOn = call.getString("speakerOn");
        audioToggle.setSpeakerOn(speakerOn.equals("true"));

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
}
