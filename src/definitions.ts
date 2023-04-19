export type AudioDevice = 'earpiece' | 'speaker';

export interface AudioTogglePlugin {
  setAudioDevice(data: { device: AudioDevice }): Promise<void>;
  reset(): Promise<void>;
}
