# @anuradev/capacitor-audio-toggle

Capacitor plugin to audio toggle

## Install

```bash
npm install @anuradev/capacitor-audio-toggle
npx cap sync
```

## API

<docgen-index>

* [`setSpeakerOn(...)`](#setspeakeron)
* [`start()`](#start)
* [`stop()`](#stop)
* [`addListener('speakerOn', ...)`](#addlistenerspeakeron-)
* [`addListener('volumeChange', ...)`](#addlistenervolumechange-)
* [`removeAllListeners()`](#removealllisteners)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### setSpeakerOn(...)

```typescript
setSpeakerOn(data: { speakerOn: boolean; }) => Promise<void>
```

| Param      | Type                                 |
| ---------- | ------------------------------------ |
| **`data`** | <code>{ speakerOn: boolean; }</code> |

--------------------


### start()

```typescript
start() => Promise<void>
```

--------------------


### stop()

```typescript
stop() => Promise<void>
```

--------------------


### addListener('speakerOn', ...)

```typescript
addListener(eventName: 'speakerOn', listenerFunc: (data: { status: boolean; }) => void) => Promise<PluginListenerHandle>
```

| Param              | Type                                                 |
| ------------------ | ---------------------------------------------------- |
| **`eventName`**    | <code>'speakerOn'</code>                             |
| **`listenerFunc`** | <code>(data: { status: boolean; }) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### addListener('volumeChange', ...)

```typescript
addListener(eventName: 'volumeChange', listenerFunc: (data: { streamType: number; newVolume: number; oldVolume: number; }) => void) => Promise<PluginListenerHandle>
```

| Param              | Type                                                                                          |
| ------------------ | --------------------------------------------------------------------------------------------- |
| **`eventName`**    | <code>'volumeChange'</code>                                                                   |
| **`listenerFunc`** | <code>(data: { streamType: number; newVolume: number; oldVolume: number; }) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### removeAllListeners()

```typescript
removeAllListeners() => Promise<void>
```

--------------------


### Interfaces


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |

</docgen-api>
