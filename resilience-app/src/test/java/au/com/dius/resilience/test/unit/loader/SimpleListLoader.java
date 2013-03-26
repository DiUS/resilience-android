package au.com.dius.resilience.test.unit.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import au.com.dius.resilience.loader.AbstractAsyncListLoader;
import au.com.dius.resilience.observer.IntentBasedLoaderNotifierBroadcastReceiver;

import java.util.List;

/*
 * A simple implementation of AbstractAsyncListLoader for testing
 * methods in the abstract class.
 */
public class SimpleListLoader extends AbstractAsyncListLoader<Object> {
  protected SimpleListLoader(Context context) {
    super(context);
  }

  @Override
  protected BroadcastReceiver createBroadcastReceiver() {
    return new IntentBasedLoaderNotifierBroadcastReceiver(this, new IntentFilter());
  }

  @Override
  public List<Object> loadInBackground() {
    return null;
  }
}
