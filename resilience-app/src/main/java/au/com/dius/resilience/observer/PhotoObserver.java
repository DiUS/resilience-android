package au.com.dius.resilience.observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import au.com.dius.resilience.loader.PhotoListLoader;

public class PhotoObserver extends BroadcastReceiver{

  private final PhotoListLoader loader;

  public PhotoObserver(PhotoListLoader loader) {
    this.loader = loader;

    IntentFilter filter = new IntentFilter(PhotoListLoader.PHOTO_LOADED);
    loader.getContext().registerReceiver(this, filter);
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    loader.onContentChanged();
  }
}
