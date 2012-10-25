package au.com.dius.resilience.observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import au.com.dius.resilience.loader.IncidentListLoader;

public class IncidentListObserver extends BroadcastReceiver {

  private static final String LOG_TAG = IncidentListObserver.class.getName();

  private IncidentListLoader loader;

  public IncidentListObserver(IncidentListLoader loader) {
    this.loader = loader;

    IntentFilter filter = new IntentFilter(IncidentListLoader.INCIDENT_LIST_LOADER_FILTER);
    loader.getContext().registerReceiver(this, filter);
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d(LOG_TAG, "Received refresh broadcast");
    loader.onContentChanged();
  }
}
