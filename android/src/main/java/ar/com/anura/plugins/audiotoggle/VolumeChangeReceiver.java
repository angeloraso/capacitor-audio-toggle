package ar.com.anura.plugins.audiotoggle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Objects;

public class VolumeChangeReceiver extends BroadcastReceiver {

  private final String TAG = "VolumeChangeReceiver";

  @Override
  public void onReceive(Context context, Intent intent) {
    if (Objects.equals(intent.getAction(), "android.media.VOLUME_CHANGED_ACTION")) {
      int streamType = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
      int newVolume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", -1);
      int oldVolume = intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", -1);

      Log.d(TAG, "Stream type: " + streamType + ", Old: " + oldVolume + ", New: " + newVolume);
      assert AudioToggle.volumeChangeEventListener != null;
      AudioToggle.volumeChangeEventListener.onVolumeChangeEvent(streamType, newVolume, oldVolume);
    }
  }
}
