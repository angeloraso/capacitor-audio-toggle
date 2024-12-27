import type { PluginListenerHandle } from "@capacitor/core";

export interface AudioTogglePlugin {
  setSpeakerOn(data: { speakerOn: boolean }): Promise<void>;
  reset(): Promise<void>;
  addListener(
    eventName: 'speakerOn',
    listenerFunc: (data: {status: boolean}) => void,
  ): Promise<PluginListenerHandle>;
  removeAllListeners(): Promise<void>;
}
