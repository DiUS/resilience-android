package au.com.dius.resilience.ui.activity;

import android.app.LoaderManager;
import android.content.IntentFilter;
import android.content.Loader;
import android.os.Bundle;
import au.com.dius.resilience.R;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.loader.ServiceRequestLoader;
import au.com.dius.resilience.observer.LocationUpdatedMapBroadcastReceiver;
import au.com.justinb.open311.model.ServiceRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

import java.util.List;

@ContentView(R.layout.activity_map_view)
public class MapViewActivity extends RoboActivity implements LoaderManager.LoaderCallbacks<List<ServiceRequest>> {

  private GoogleMap map;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (map == null) {
      map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
    }

    registerReceiver(new LocationUpdatedMapBroadcastReceiver(), new IntentFilter(Intents.RESILIENCE_LOCATION_UPDATED));

    getLoaderManager().initLoader(ServiceRequestLoader.SERVICE_REQUEST_LIST_LOADER, null, this);
  }

  @Override
  public Loader<List<ServiceRequest>> onCreateLoader(int id, Bundle args) {
    return new ServiceRequestLoader(this);
  }

  @Override
  public void onLoadFinished(Loader<List<ServiceRequest>> loader, List<ServiceRequest> data) {

    if (loader.getId() != ServiceRequestLoader.SERVICE_REQUEST_LIST_LOADER) {
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
    map.clear();
  }
}
