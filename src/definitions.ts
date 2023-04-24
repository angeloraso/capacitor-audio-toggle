export interface AudioTogglePlugin {
  setSpeakerOn(data: { speakerOn: boolean }): Promise<void>;
  reset(): Promise<void>;
}
