package au.com.dius.resilience.ui.activity;

import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import au.com.dius.resilience.R;
import au.com.dius.resilience.loader.IncidentListLoader;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.Repository;
import au.com.dius.resilience.ui.map.IncidentOverlay;
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
    IncidentOverlay overlay = new IncidentOverlay(itemIcon, mapView);
    overlay.populateWith(data);

    mapView.getOverlays().clear();

    if (overlay.hasItems()) {
      mapView.getOverlays().add(overlay);
      mapView.invalidate();
    }
  }

  @Override
  public void onLoaderReset(Loader<List<Incident>> loader) {
  }
}
