# @anuradev/capacitor-audio-toggle

Capacitor plugin to audio toggle

## Install

```bash
npm install @anuradev/capacitor-audio-toggle
npx cap sync
```

## API

<docgen-index>

* [`setAudioDevice(...)`](#setaudiodevice)
* [`reset()`](#reset)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### setAudioDevice(...)

```typescript
setAudioDevice(data: { device: AudioDevice; }) => Promise<void>
```

| Param      | Type                                                             |
| ---------- | ---------------------------------------------------------------- |
| **`data`** | <code>{ device: <a href="#audiodevice">AudioDevice</a>; }</code> |

--------------------


### reset()

```typescript
reset() => Promise<void>
```

--------------------


### Type Aliases


#### AudioDevice

<code>'earpiece' | 'speaker'</code>

</docgen-api>
