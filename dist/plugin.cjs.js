'use strict';

var core = require('@capacitor/core');

const AudioToggle = core.registerPlugin('AudioToggle', {
    web: () => Promise.resolve().then(function () { return web; }).then(m => new m.AudioToggleWeb()),
});

class AudioToggleWeb extends core.WebPlugin {
    async setSpeakerOn() {
        throw this.unimplemented('Not implemented on web.');
    }
    async start() {
        throw this.unimplemented('Not implemented on web.');
    }
    async stop() {
        throw this.unimplemented('Not implemented on web.');
    }
}

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    AudioToggleWeb: AudioToggleWeb
});

exports.AudioToggle = AudioToggle;
//# sourceMappingURL=plugin.cjs.js.map
