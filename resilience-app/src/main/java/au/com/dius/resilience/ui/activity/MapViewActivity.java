package au.com.dius.resilience.ui.activity;

import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import au.com.dius.resilience.R;
import au.com.dius.resilience.loader.ServiceRequestLoader;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Point;
import au.com.dius.resilience.persistence.repository.Repository;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;
import au.com.dius.resilience.ui.Themer;
import au.com.dius.resilience.ui.map.IncidentOverlay;
import au.com.justinb.open311.model.ServiceRequest;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.inject.Inject;
import roboguice.activity.RoboMapActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.util.List;

/**
 * @author georgepapas
 */
@ContentView(R.layout.activity_map_view)
public class MapViewActivity extends RoboMapActivity implements LoaderManager.LoaderCallbacks<List<ServiceRequest>> {

  public static final int ZOOM_LEVEL = 17;
  @InjectView(R.id.map_view)
  private MapView mapView;

  @InjectResource(R.drawable.blue_poi)
  private Drawable itemIcon;

  @Inject
  private Repository repository;

  @Inject
  private PreferenceAdapter preferenceAdapter;

  @Inject
  private Themer themer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getLoaderManager().initLoader(ServiceRequestLoader.SERVICE_REQUEST_LIST_LOADER, null, this);
    Point lastKnownLocation = preferenceAdapter.retrieveLastKnownLocation();

    if (lastKnownLocation != null) {
      GeoPoint geoPoint = new GeoPoint((int) (lastKnownLocation.getLatitude() * 1E6), (int) (lastKnownLocation.getLongitude() * 1E6));
      mapView.setBuiltInZoomControls(true);
      mapView.getController().setCenter(geoPoint);
      mapView.getController().setZoom(ZOOM_LEVEL);
    }
  }

  @Override
  protected boolean isRouteDisplayed() {
    return false;
  }

  @Override
  public Loader<List<ServiceRequest>> onCreateLoader(int id, Bundle args) {
    return new ServiceRequestLoader(this);
  }

  @Override
  public void onLoadFinished(Loader<List<ServiceRequest>> loader, List<ServiceRequest> data) {
    IncidentOverlay overlay = new IncidentOverlay(itemIcon, mapView, repository);
    overlay.populateWith(data);

    mapView.getOverlays().clear();

    if (overlay.hasItems()) {
      mapView.getOverlays().add(overlay);
      mapView.invalidate();
    }
  }

  @Override
  public void onLoaderReset(Loader<List<ServiceRequest>> loader) {
  }
}
