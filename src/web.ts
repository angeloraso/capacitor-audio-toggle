import { WebPlugin } from '@capacitor/core';

import type { AudioTogglePlugin } from './definitions';

export class AudioToggleWeb extends WebPlugin implements AudioTogglePlugin {
  async setSpeakerOn(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  } 

  async reset(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  } 
}
