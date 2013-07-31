package au.com.dius.resilience.ui.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;
import au.com.dius.resilience.R;
import au.com.dius.resilience.intent.Extras;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.loader.ServiceRequestLoader;
import au.com.dius.resilience.loader.event.LoadingEvent;
import au.com.dius.resilience.loader.event.PageResetEvent;
import au.com.dius.resilience.loader.event.ServiceRequestLoadFailed;
import au.com.dius.resilience.location.LocationBroadcaster;
import au.com.dius.resilience.location.event.LocationUpdatedEvent;
import au.com.justinb.open311.model.ServiceRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.inject.Inject;
import com.squareup.otto.Subscribe;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_map_view)
public class MapViewActivity extends RoboActivity implements LoaderManager.LoaderCallbacks<List<ServiceRequest>>, GoogleMap.OnInfoWindowClickListener {

  public static final float ZOOM_LEVEL = 16.0f;

  private static final int CROUTON_DURATION = 2000;
  public static final int CURRENT_POSITION_DIAMETER = 10;

  private GoogleMap map;

  @Inject
  private LocationBroadcaster locationBroadcaster;

  private boolean loaded = false;

  private CircleOptions circleOptions;

  private Map<String, ServiceRequest> markerToServiceRequestMap;

  private ServiceRequestLoader serviceRequestLoader;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (map == null) {
      map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

      if (map == null) {
        return;
      }

      map.setOnInfoWindowClickListener(this);
    }

    markerToServiceRequestMap = new HashMap<String, ServiceRequest>();

    locationBroadcaster.subscribe(this);
    getLoaderManager().initLoader(ServiceRequestLoader.SERVICE_REQUEST_LIST_LOADER, null, this);
    serviceRequestLoader.subscribe(this);
    locationBroadcaster.subscribe(serviceRequestLoader);
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

  @Subscribe
  public void onLoadingEvent(LoadingEvent event) {
    Crouton.cancelAllCroutons();
    createLoadingCrouton().show();
  }

  @Override
  public void onDestroy() {
    serviceRequestLoader.unsubscribe(this);
    locationBroadcaster.stopPolling();
    locationBroadcaster.unsubscribe(this);
    locationBroadcaster.unsubscribe(serviceRequestLoader);
    Crouton.cancelAllCroutons();
    super.onDestroy();
  }

  @Override
  public Loader<List<ServiceRequest>> onCreateLoader(int id, Bundle args) {
    serviceRequestLoader = new ServiceRequestLoader(this);
    return serviceRequestLoader;
  }

  @Subscribe
  public void onLocationUpdatedEvent(LocationUpdatedEvent event) {
    if (!loaded && map != null) {
      Intent intent = new Intent(Intents.RESILIENCE_INCIDENT_CREATED);
      sendBroadcast(intent);

      Location location = event.getLocation();
      LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
      map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL));

      circleOptions = new CircleOptions()
        .center(latLng)
        .radius(CURRENT_POSITION_DIAMETER)
        .strokeWidth(1.0f)
        .strokeColor(Color.WHITE)
        .fillColor(Color.argb(100, 0, 20, 255));
      map.addCircle(circleOptions);

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
        Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(serviceRequest.getServiceName()));
        markerToServiceRequestMap.put(marker.getId(), serviceRequest);
      }
    }
  }

  @Override
  public void onLoaderReset(Loader<List<ServiceRequest>> loader) {

    if (map == null) {
      return;
    }

    clearMarkers();
  }

  @Subscribe
  public void onPageResetEvent(PageResetEvent event) {

    if (map == null) {
      return;
    }

    clearMarkers();
  }

  @Subscribe
  public void onServiceRequestLoadFailedEvent(ServiceRequestLoadFailed event) {
    Crouton.makeText(this, "Load failed, please try again later.", Style.ALERT).show();
  }

  private void clearMarkers() {
    map.clear();
    map.addCircle(circleOptions);
  }

  @Override
  public void onInfoWindowClick(Marker marker) {
    ServiceRequest serviceRequest = markerToServiceRequestMap.get(marker.getId());
    Intent intent = new Intent(this, ViewServiceRequestActivity.class);
    intent.putExtra(Extras.SERVICE_REQUEST, serviceRequest);
    startActivity(intent);
  }
}
