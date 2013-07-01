package au.com.dius.resilience.ui.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import au.com.dius.resilience.R;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.loader.ServiceRequestLoader;
import au.com.dius.resilience.loader.event.ServiceRequestLoadFailed;
import au.com.dius.resilience.location.LocationBroadcaster;
import au.com.dius.resilience.location.event.LocationUpdatedEvent;
import au.com.justinb.open311.model.ServiceRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.inject.Inject;
import com.squareup.otto.Subscribe;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

import java.util.List;

@ContentView(R.layout.activity_map_view)
public class MapViewActivity extends RoboActivity implements LoaderManager.LoaderCallbacks<List<ServiceRequest>> {

  public static final float ZOOM_LEVEL = 16.0f;
  private GoogleMap map;

  @Inject
  private LocationBroadcaster locationBroadcaster;

  private boolean loaded = false;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (map == null) {
      map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
    }

    getLoaderManager().initLoader(ServiceRequestLoader.SERVICE_REQUEST_LIST_LOADER, null, this);
  }

  @Override
  public void onResume() {
    super.onResume();
    locationBroadcaster.startPolling();
  }

  @Override
  public void onPause() {
    locationBroadcaster.stopPolling();
    super.onPause();
  }

  @Override
  public Loader<List<ServiceRequest>> onCreateLoader(int id, Bundle args) {
    ServiceRequestLoader serviceRequestLoader = new ServiceRequestLoader(this);
    serviceRequestLoader.subscribe(this);
    locationBroadcaster.subscribe(serviceRequestLoader);
    locationBroadcaster.subscribe(this);
    return serviceRequestLoader;
  }

  @Subscribe
  public void onLocationUpdatedEvent(LocationUpdatedEvent event) {
    if (!loaded) {
      Intent intent = new Intent(Intents.RESILIENCE_INCIDENT_CREATED);
      sendBroadcast(intent);

      Location location = event.getLocation();
      LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
      map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL));

      loaded = true;
    }
  }

  @Override
  public void onLoadFinished(Loader<List<ServiceRequest>> loader, List<ServiceRequest> data) {

    if (loader.getId() != ServiceRequestLoader.SERVICE_REQUEST_LIST_LOADER || map == null) {
      return;
    }

    plotServiceRequestMarkers(data);
  }

  private void plotServiceRequestMarkers(List<ServiceRequest> data) {
    for (ServiceRequest serviceRequest : data) {
      if (serviceRequest.getLat() != null && serviceRequest.getLong() != null) {
        LatLng latLng = new LatLng(serviceRequest.getLat(), serviceRequest.getLong());
        map.addMarker(new MarkerOptions().position(latLng).title(serviceRequest.getServiceName()));
      }
    }
  }

  @Override
  public void onLoaderReset(Loader<List<ServiceRequest>> loader) {

    ((ServiceRequestLoader)loader).unsubscribe(this);
    locationBroadcaster.unsubscribe(loader);

    if (map == null) {
      return;
    }

    map.clear();
  }

  @Subscribe
  public void onServiceRequestLoadFailedEvent(ServiceRequestLoadFailed event) {
    Toast.makeText(this, "Load failed, please try again later.", Toast.LENGTH_LONG);
  }
}
