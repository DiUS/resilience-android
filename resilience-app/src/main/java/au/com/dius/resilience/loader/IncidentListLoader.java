package au.com.dius.resilience.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.observer.IntentBasedLoaderNotifierBroadcastReceiver;
import au.com.dius.resilience.persistence.repository.Repository;

import java.util.List;

public class IncidentListLoader extends AbstractAsyncListLoader<Incident> {

  private static final String LOG_TAG = IncidentListLoader.class.getName();

  public static final int INCIDENT_LIST_LOADER = 0;

  public IncidentListLoader(Context context, Repository repository) {
    super(context, repository);
  }

  @Override
  public List<Incident> loadInBackground() {
    Log.d(LOG_TAG, "Loader loading data");

    return repository.findIncidents();
  }

  @Override
  protected BroadcastReceiver createBroadcastReceiver() {
    return new IntentBasedLoaderNotifierBroadcastReceiver(this, new IntentFilter(Intents.RESILIENCE_INCIDENT_CREATED));
  }
}
