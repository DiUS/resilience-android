package au.com.dius.resilience.ui.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.intent.Extras;
import au.com.dius.resilience.loader.ServiceRequestLoader;
import au.com.dius.resilience.ui.adapter.ListViewAdapter;
import au.com.justinb.open311.model.ServiceRequest;
import roboguice.activity.RoboListActivity;

import java.util.ArrayList;
import java.util.List;

public class ServiceRequestListActivity extends RoboListActivity implements LoaderManager.LoaderCallbacks<List<ServiceRequest>> {

  private static final String LOG_TAG = ServiceRequestListActivity.class.getName();

  private static final int MAX_RESULT_SIZE = 10;

  private ListViewAdapter adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    adapter = new ListViewAdapter(this, R.layout.service_request_list_view_item, new ArrayList<ServiceRequest>());
    super.setListAdapter(adapter);

    getListView().setDividerHeight(0);

    getLoaderManager().initLoader(ServiceRequestLoader.SERVICE_REQUEST_LIST_LOADER, null, this);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {

    ServiceRequest serviceRequest = adapter.getItem(position);

    Bundle bundle = new Bundle();
    // FIXME - use parceable instead of serializable since it's faster.
    bundle.putSerializable(Extras.SERVICE_REQUEST, serviceRequest);

    Intent intent = new Intent(this, ViewServiceRequestActivity.class);
    intent.putExtras(bundle);

    startActivity(intent);
  }

  @Override
  public Loader<List<ServiceRequest>> onCreateLoader(int i, Bundle bundle) {
    Log.d(LOG_TAG, "Creating ServiceRequestLoader.");
    return new ServiceRequestLoader(this);
  }

  @Override
  public void onLoadFinished(Loader<List<ServiceRequest>> listLoader, List<ServiceRequest> incidentList) {
    Log.d(LOG_TAG, "Adding " + incidentList + " Objects to the UI.");

    // TODO - a better solution would be to load more on demand. Just going to set a
    // hard limit for now.
    List<ServiceRequest> limitedList = incidentList.subList(0, MAX_RESULT_SIZE);
    adapter.setData(limitedList);
  }

  @Override
  public void onLoaderReset(Loader<List<ServiceRequest>> listLoader) {
    adapter.setData(null);
  }
}
