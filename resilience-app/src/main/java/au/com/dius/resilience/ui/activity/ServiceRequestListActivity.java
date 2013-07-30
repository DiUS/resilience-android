package au.com.dius.resilience.ui.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.intent.Extras;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.loader.ServiceRequestLoader;
import au.com.dius.resilience.loader.event.LoadingEvent;
import au.com.dius.resilience.loader.event.PageResetEvent;
import au.com.dius.resilience.loader.event.ServiceRequestLoadFailed;
import au.com.dius.resilience.location.LocationBroadcaster;
import au.com.dius.resilience.location.event.LocationUpdatedEvent;
import au.com.dius.resilience.persistence.async.AnonymousRepeatableTask;
import au.com.dius.resilience.ui.adapter.ListViewAdapter;
import au.com.dius.resilience.util.Logger;
import au.com.justinb.open311.model.ServiceRequest;
import com.google.inject.Inject;
import com.squareup.otto.Subscribe;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

@ContentView(R.layout.activity_service_request_list)
public class ServiceRequestListActivity extends RoboActivity implements LoaderManager.LoaderCallbacks<List<ServiceRequest>>
  , AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

  private static final int CROUTON_DURATION = 2000;

  private ListViewAdapter adapter;

  private long MIN_REFRESH_TIME = 2000;

  private boolean isLoading = false;

  private AnonymousRepeatableTask blockRefreshTask;

  @InjectView(R.id.service_request_list_view)
  private ListView listView;

  @Inject
  private LocationBroadcaster locationBroadcaster;

  private ServiceRequestLoader serviceRequestLoader;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    listView.setOnItemClickListener(this);

    adapter = new ListViewAdapter(this, R.layout.service_request_list_view_item, new ArrayList<ServiceRequest>());
    listView.setAdapter(adapter);

    initBlockingTask();

    listView.setDividerHeight(0);
    listView.setOnScrollListener(this);

    getLoaderManager().initLoader(ServiceRequestLoader.SERVICE_REQUEST_LIST_LOADER, null, this);

    serviceRequestLoader.subscribe(this);
    locationBroadcaster.subscribe(serviceRequestLoader);
    locationBroadcaster.subscribe(this);
    locationBroadcaster.startPolling();
  }

  // TODO - move to factory, or something.
  private Crouton createLoadingCrouton() {
    Style style = new Style.Builder()
      .setGravity(Gravity.CENTER)
      .setBackgroundColor(R.color.background)
      .setTextColor(android.R.color.black)
      .setConfiguration(new Configuration.Builder().setDuration(CROUTON_DURATION).build())
      .build();

    return Crouton.makeText(this, "Loading incidents..", style);
  }

  @Override
  public void onDestroy() {
    locationBroadcaster.stopPolling();
    locationBroadcaster.unsubscribe(this);
    locationBroadcaster.unsubscribe(serviceRequestLoader);
    serviceRequestLoader.unsubscribe(this);
    Crouton.cancelAllCroutons();
    super.onDestroy();
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

  @Subscribe
  public void onServiceRequestLoadFailedEvent(ServiceRequestLoadFailed event) {
    Crouton.makeText(this, "Load failed, please try again later.", Style.ALERT).show();
  }

  @Subscribe
  public void onPageResetEvent(PageResetEvent event) {
    adapter.clear();
    adapter.setData(null);
  }

  @Subscribe
  public void onLoadingEvent(LoadingEvent event) {
    Crouton.cancelAllCroutons();
    createLoadingCrouton().show();
  }

  @Override
  public Loader<List<ServiceRequest>> onCreateLoader(int i, Bundle bundle) {
    Logger.d(this, "Creating ServiceRequestLoader.");
    serviceRequestLoader = new ServiceRequestLoader(this);
    return serviceRequestLoader;
  }

  @Subscribe
  public void onLocationUpdatedEvent(LocationUpdatedEvent event) {
    if (listView != null && listView.getCount() == 0) {
      Intent intent = new Intent(Intents.RESILIENCE_INCIDENT_CREATED);
      sendBroadcast(intent);
    }
  }

  @Override
  public void onLoadFinished(Loader<List<ServiceRequest>> listLoader, List<ServiceRequest> incidentList) {

    if (listLoader.getId() != ServiceRequestLoader.SERVICE_REQUEST_LIST_LOADER) {
      return;
    }

    Logger.d(this, "Adding " + incidentList.size() + " incidents to UI.");

    adapter.addAll(incidentList);
  }

  @Override
  public void onLoaderReset(Loader<List<ServiceRequest>> listLoader) {
    adapter.clear();
    adapter.setData(null);
  }

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {

    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

      final Loader<Object> serviceRequestLoader = getLoaderManager().getLoader(ServiceRequestLoader.SERVICE_REQUEST_LIST_LOADER);

      if (serviceRequestLoader == null || isLoading) {
        return;
      }

      Crouton.cancelAllCroutons();

      if (adapter.getCount() > 0 && listView.getLastVisiblePosition() == adapter.getCount() - 1) {

        isLoading = true;
        serviceRequestLoader.onContentChanged();

        blockRefreshTask.execute();
      }
    }
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    ServiceRequest serviceRequest = adapter.getItem(position);

    Bundle bundle = new Bundle();
    // FIXME - use parceable instead of serializable since it's faster.
    bundle.putSerializable(Extras.SERVICE_REQUEST, serviceRequest);

    Intent intent = new Intent(this, ViewServiceRequestActivity.class);
    intent.putExtras(bundle);

    startActivity(intent);
  }

  @Override
  public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
  }
}
