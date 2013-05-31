package au.com.dius.resilience.ui.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;
import au.com.dius.resilience.R;
import au.com.dius.resilience.intent.Extras;
import au.com.dius.resilience.loader.ServiceRequestLoader;
import au.com.dius.resilience.persistence.async.AnonymousRepeatableTask;
import au.com.dius.resilience.ui.adapter.ListViewAdapter;
import au.com.dius.resilience.util.Logger;
import au.com.justinb.open311.model.ServiceRequest;
import org.apache.commons.lang.StringUtils;
import roboguice.activity.RoboListActivity;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class ServiceRequestListActivity extends RoboListActivity implements LoaderManager.LoaderCallbacks<List<ServiceRequest>>
  , AbsListView.OnScrollListener {

  private ListViewAdapter adapter;

  private long MIN_REFRESH_TIME = 5000;

  private boolean isLoading = false;

  private AnonymousRepeatableTask blockRefreshTask;

  private Toast toast;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    adapter = new ListViewAdapter(this, R.layout.service_request_list_view_item, new ArrayList<ServiceRequest>());
    super.setListAdapter(adapter);

    toast = Toast.makeText(this, StringUtils.EMPTY, Toast.LENGTH_SHORT);

    initBlockingTask();

    getListView().setDividerHeight(0);
    getListView().setOnScrollListener(this);

    getLoaderManager().initLoader(ServiceRequestLoader.SERVICE_REQUEST_LIST_LOADER, null, this);
  }

  private void initBlockingTask() {
    blockRefreshTask = new AnonymousRepeatableTask(new Runnable() {
      @Override
      public void run() {
        try {
          sleep(MIN_REFRESH_TIME);
        } catch (InterruptedException e) {
          Logger.w("Interrupted while waiting for refresh timeout!");
        }
        isLoading = false;
      }
    });
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
    Logger.d(this, "Creating ServiceRequestLoader.");
    return new ServiceRequestLoader(this);
  }

  @Override
  public void onLoadFinished(Loader<List<ServiceRequest>> listLoader, List<ServiceRequest> incidentList) {

    if (incidentList.size() == 0) {
      String toastText = adapter.getCount() > 0 ? getString(R.string.list_view_no_more_incidents) :
        getString(R.string.list_view_no_incidents);

      toast.setText(toastText);
      toast.show();
    }

    adapter.addAll(incidentList);
  }

  @Override
  public void onLoaderReset(Loader<List<ServiceRequest>> listLoader) {
    adapter.setData(null);
  }

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {

    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

      final Loader<Object> serviceRequestLoader = getLoaderManager().getLoader(ServiceRequestLoader.SERVICE_REQUEST_LIST_LOADER);

      if (serviceRequestLoader == null || isLoading) {
        return;
      }

      if (getListView().getLastVisiblePosition() == adapter.getCount() - 1) {

        toast.setText(getString(R.string.list_view_loading_more));
        toast.show();

        isLoading = true;
        serviceRequestLoader.onContentChanged();

        blockRefreshTask.execute();
      }
    }
  }

  @Override
  public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
  }
}
