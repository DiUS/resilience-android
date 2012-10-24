package au.com.dius.resilience.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.model.Impact;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.observer.IncidentListObserver;
import au.com.dius.resilience.persistence.repository.impl.ParseIncidentAdapter;
import com.parse.Parse;
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
  private IncidentListObserver refreshObserver;

  public IncidentListLoader(Context context) {
    super(context);
  }

  @Override
  public List<Incident> loadInBackground() {
    Log.d(LOG_TAG, "Loader loading data");

    ParseQuery query = new ParseQuery(Constants.TABLE_INCIDENT);
    List<ParseObject> parseObjects = new ArrayList<ParseObject>();
    try {
      parseObjects = query.find();
    } catch (ParseException e) {
      Log.d(LOG_TAG, "Loading all incidents failed with: ", e);
      throw new RuntimeException(e);
    }

    ParseIncidentAdapter parseIncidentAdapter = new ParseIncidentAdapter();
    List<Incident> incidents = new ArrayList<Incident>();
    for (ParseObject parseIncident : parseObjects) {
      incidents.add(parseIncidentAdapter.deserialise(parseIncident));
    }

    return incidents;
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
  protected void onStartLoading() {
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
  protected void onReset() {
    super.onReset();
    Log.d(LOG_TAG, "Resetting loader.");

      data = null;

    // Stop observer here
    getContext().unregisterReceiver(refreshObserver);
    refreshObserver = null;
  }
}
