package au.com.dius.resilience.ui.activity;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.loader.ServiceRequestLoader;
import au.com.dius.resilience.ui.adapter.ListViewAdapter;
import au.com.justinb.open311.model.ServiceRequest;
import roboguice.activity.RoboListActivity;

import java.util.ArrayList;
import java.util.List;

public class ServiceRequestListActivity extends RoboListActivity implements LoaderManager.LoaderCallbacks<List<ServiceRequest>> {

  private static final String LOG_TAG = ServiceRequestListActivity.class.getName();

  private ListViewAdapter adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    adapter = new ListViewAdapter(this, R.layout.incident_list_view_item, new ArrayList<ServiceRequest>());
    super.setListAdapter(adapter);

    getListView().setSelector(R.drawable.selector_background);
    getListView().setDividerHeight(0);

    getLoaderManager().initLoader(ServiceRequestLoader.SERVICE_REQUEST_LIST_LOADER, null, this);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    ServiceRequest incident = (ServiceRequest) getListAdapter().getItem(position);
    // TODO
//    Intent viewIncident = new Intent(this, ViewIncidentActivity.class);
//    viewIncident.putExtra(EXTRA_INCIDENT, incident);
//    startActivityForResult(viewIncident, 0);
  }

  @Override
  public Loader<List<ServiceRequest>> onCreateLoader(int i, Bundle bundle) {
    Log.d(LOG_TAG, "Creating IncidentListLoader.");
    return new ServiceRequestLoader(this);
  }

  @Override
  public void onLoadFinished(Loader<List<ServiceRequest>> listLoader, List<ServiceRequest> incidentList) {
    Log.d(LOG_TAG, "Adding " + incidentList + " Objects to the UI.");
    adapter.setData(incidentList);
  }

  @Override
  public void onLoaderReset(Loader<List<ServiceRequest>> listLoader) {
    adapter.setData(null);
  }
}
