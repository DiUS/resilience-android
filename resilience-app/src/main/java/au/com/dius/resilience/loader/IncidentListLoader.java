package au.com.dius.resilience.loader;

import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.util.Log;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.observer.IncidentListObserver;
import au.com.dius.resilience.persistence.repository.Repository;
import au.com.dius.resilience.persistence.repository.impl.ParseIncidentAdapter;
import au.com.dius.resilience.persistence.repository.impl.RepositoryCommand;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class IncidentListLoader extends AsyncTaskLoader<List<Incident>> {

  public static final int INCIDENT_LIST_LOADER = 0;
  public static final String INCIDENT_LIST_LOADER_FILTER = "INCIDENT_LIST_LOADER_FILTER";

  private static final String LOG_TAG = IncidentListLoader.class.getName();

  private List<Incident> data;
  private BroadcastReceiver refreshObserver;

  private Repository repository;

  public IncidentListLoader(Context context, Repository repository) {
    super(context);
    this.repository = repository;
  }

  @Override
  public List<Incident> loadInBackground() {
    Log.d(LOG_TAG, "Loader loading data");

    return repository.findIncidents();
  }

  @Override
  public void deliverResult(List<Incident> data) {
    Log.d(LOG_TAG, "Loader delivering results");

    // Keep a reference to the old data so it is not garbage collected
    // during delivery.
    List<Incident> oldData = data;
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
      refreshObserver = new IncidentListObserver(this);
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
