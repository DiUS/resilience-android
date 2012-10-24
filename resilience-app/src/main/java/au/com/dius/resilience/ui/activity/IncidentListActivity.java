package au.com.dius.resilience.ui.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.loader.IncidentListLoader;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.IncidentRepository;
import au.com.dius.resilience.ui.adapter.ListViewAdapter;
import com.google.inject.Inject;
import roboguice.activity.RoboListActivity;

import java.util.ArrayList;
import java.util.List;

public class IncidentListActivity extends RoboListActivity implements LoaderManager.LoaderCallbacks<List<Incident>> {

  private static final String LOG_TAG = IncidentListActivity.class.getName();

  @Inject
  private IncidentRepository incidentRepository;

  private ListViewAdapter adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    adapter = new ListViewAdapter(this, R.layout.incident_list_view_item, new ArrayList<Incident>());
    super.setListAdapter(adapter);

    Log.d(LOG_TAG, "Calling load manager");
    getLoaderManager().initLoader(IncidentListLoader.INCIDENT_LIST_LOADER, null, this);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    Incident incident = (Incident) getListAdapter().getItem(position);
    Intent viewIncident = new Intent(this, ViewIncidentActivity.class);
    viewIncident.putExtra("incident", incident);
    startActivityForResult(viewIncident, 0);
  }

  @Override
  public Loader<List<Incident>> onCreateLoader(int i, Bundle bundle) {
    Log.d(LOG_TAG, "Creating IncidentListLoader.");
    return new IncidentListLoader(this);
  }

  @Override
  public void onLoadFinished(Loader<List<Incident>> listLoader, List<Incident> incidentList) {
    Log.d(LOG_TAG, "Adding " + incidentList + " Objects to the UI.");
    adapter.setData(incidentList);
  }

  @Override
  public void onLoaderReset(Loader<List<Incident>> listLoader) {
    adapter.setData(null);
  }
}
