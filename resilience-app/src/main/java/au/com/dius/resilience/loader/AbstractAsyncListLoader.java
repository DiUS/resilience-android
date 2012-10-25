package au.com.dius.resilience.loader;

import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.util.Log;
import au.com.dius.resilience.persistence.repository.Repository;

import java.util.List;

public abstract class AbstractAsyncListLoader<T> extends AsyncTaskLoader<List<T>> {

  private static final String LOG_TAG = AbstractAsyncListLoader.class.getName();

  private List<T> data;
  private BroadcastReceiver refreshObserver;

  protected Repository repository;

  protected abstract BroadcastReceiver createBroadcastReceiver();

  protected AbstractAsyncListLoader(Context context, Repository repository) {
    super(context);
    this.repository = repository;
  }

  @Override
  public void deliverResult(List<T> data) {
    Log.d(LOG_TAG, "Loader delivering results");

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
    Log.d(LOG_TAG, "Starting loader");

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
    Log.d(LOG_TAG, "Resetting loader, clearing data and unregistering observer.");

    data = null;

    getContext().unregisterReceiver(refreshObserver);
    refreshObserver = null;
  }
}
