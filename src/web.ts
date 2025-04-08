import { WebPlugin } from '@capacitor/core';

import type { AudioTogglePlugin } from './definitions';

export class AudioToggleWeb extends WebPlugin implements AudioTogglePlugin {
  async setSpeakerOn(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  } 

  async start(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  } 

  async stop(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  } 
}
