package au.com.dius.resilience.ui.activity;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import au.com.dius.resilience.R;
import au.com.dius.resilience.loader.ServiceRequestLoader;
import au.com.justinb.open311.model.ServiceRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.apache.commons.lang.StringUtils;
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

    getLoaderManager().initLoader(ServiceRequestLoader.SERVICE_REQUEST_LIST_LOADER, null, this);
  }

  @Override
  public Loader<List<ServiceRequest>> onCreateLoader(int id, Bundle args) {
    return new ServiceRequestLoader(this);
  }

  @Override
  public void onLoadFinished(Loader<List<ServiceRequest>> loader, List<ServiceRequest> data) {

    for (ServiceRequest serviceRequest : data) {

      // TODO - test me!
      if (StringUtils.isNotBlank(serviceRequest.getLat()) || StringUtils.isNotBlank(serviceRequest.getLong())) {
        LatLng latLng = new LatLng(Double.parseDouble(serviceRequest.getLat()), Double.parseDouble(serviceRequest.getLong()));
        map.addMarker(new MarkerOptions().position(latLng).title(serviceRequest.getServiceName()));
      }
    }

  }

  @Override
  public void onLoaderReset(Loader<List<ServiceRequest>> loader) {
    map.clear();
  }
}
