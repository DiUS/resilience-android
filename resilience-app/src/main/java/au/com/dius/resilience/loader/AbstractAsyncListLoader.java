package au.com.dius.resilience.loader;

import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.util.Log;
import au.com.dius.resilience.util.Logger;

import java.util.List;

public abstract class AbstractAsyncListLoader<T> extends AsyncTaskLoader<List<T>> {

  private static final String LOG_TAG = AbstractAsyncListLoader.class.getName();

  private List<T> data;
  private BroadcastReceiver refreshObserver;

  protected abstract BroadcastReceiver createBroadcastReceiver();

  protected AbstractAsyncListLoader(Context context) {
    super(context);
  }

  @Override
  public void deliverResult(List<T> data) {
    Logger.d(this, "Loader delivering results");

    // Keep a reference to the old data so it is not garbage collected
    // during delivery.
    List<T> oldData = data;
    this.data = data;

    if (isStarted()) {
      super.deliverResult(data);
    }
  }

  @Override
  public void onStartLoading() {
    Logger.d(this, "Starting loader");

    if (data != null) {
      deliverResult(data);
    }

    if (refreshObserver == null) {
      refreshObserver = createBroadcastReceiver();
    }

    if (takeContentChanged() || data == null) {
      forceLoad();
    }
  }

  @Override
  public void onReset() {
    super.onReset();
    Logger.d(this, "Resetting loader, clearing data and unregistering observer.");

    data = null;

    getContext().unregisterReceiver(refreshObserver);
    refreshObserver = null;
  }
}
