package ar.com.anura.plugins.audiotoggle;

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
        Context context = getContext();
        audioToggle = new AudioToggle(activity, context);
    }

    @PluginMethod
    public void setAudioDevice(PluginCall call) {
        if (getActivity().isFinishing()) {
            call.reject("Audio toggle plugin error: App is finishing");
            return;
        }

        String device = call.getString("device");
        audioToggle.setAudioDevice(device);

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
