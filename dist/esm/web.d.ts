import { WebPlugin } from '@capacitor/core';
import type { AudioTogglePlugin } from './definitions';
export declare class AudioToggleWeb extends WebPlugin implements AudioTogglePlugin {
    setSpeakerOn(): Promise<void>;
    start(): Promise<void>;
    stop(): Promise<void>;
}
