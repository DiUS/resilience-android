package au.com.dius.resilience.ui.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.R;
import au.com.dius.resilience.loader.IncidentListLoader;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Point;
import au.com.dius.resilience.persistence.repository.Repository;
import au.com.dius.resilience.ui.map.ResilienceItemisedOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.inject.Inject;
import roboguice.activity.RoboMapActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author georgepapas
 */
  @ContentView(R.layout.activity_map_view)
  public class MapViewActivity extends RoboMapActivity implements LoaderManager.LoaderCallbacks<List<Incident>> {

  @InjectView(R.id.map_view)
  private MapView mapView;

  @InjectResource(R.drawable.blue_poi)
  private Drawable itemIcon;

  @Inject
  private Repository repository;

  private static final String LOG_TAG = MapViewActivity.class.getName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mapView.setBuiltInZoomControls(true);

    getLoaderManager().initLoader(IncidentListLoader.INCIDENT_LIST_LOADER, null, this);
  }

  @Override
  protected boolean isRouteDisplayed() {
    return false;
  }

  @Override
  public Loader<List<Incident>> onCreateLoader(int id, Bundle args) {
    return new IncidentListLoader(this, repository);
  }

  @Override
  public void onLoadFinished(Loader<List<Incident>> loader, List<Incident> data) {

    Log.d(LOG_TAG, "Load finished with " + data.size() + " incidents.");
    ResilienceItemisedOverlay overlay = new ResilienceItemisedOverlay(this, itemIcon);
    for (Incident incident : data) {
      Point point = incident.getPoint();
      if (point != null) {
        Log.d(LOG_TAG, "Adding point: " + point.getLatitude() + ", " + point.getLongitude());
        GeoPoint geoPoint = new GeoPoint((int) (point.getLatitude() * 1E6), (int) (point.getLongitude() * 1E6));
        OverlayItem overlayItem = new OverlayItem(geoPoint, incident.getCategory(), incident.getNote());
        overlay.addOverlay(overlayItem);
      }
    }

    if (overlay.hasItems()) {
      mapView.getOverlays().clear();
      mapView.getOverlays().add(overlay);
    }
  }

  @Override
  public void onLoaderReset(Loader<List<Incident>> loader) {
  }
}
