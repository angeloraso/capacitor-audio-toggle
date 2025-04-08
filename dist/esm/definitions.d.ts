import type { PluginListenerHandle } from "@capacitor/core";
export interface AudioTogglePlugin {
    setSpeakerOn(data: {
        speakerOn: boolean;
    }): Promise<void>;
    start(): Promise<void>;
    stop(): Promise<void>;
    addListener(eventName: 'speakerOn', listenerFunc: (data: {
        status: boolean;
    }) => void): Promise<PluginListenerHandle>;
    addListener(eventName: 'volumeChange', listenerFunc: (data: {
        streamType: number;
        newVolume: number;
        oldVolume: number;
    }) => void): Promise<PluginListenerHandle>;
    removeAllListeners(): Promise<void>;
}
