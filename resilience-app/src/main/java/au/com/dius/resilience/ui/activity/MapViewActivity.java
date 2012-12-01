package au.com.dius.resilience.ui.activity;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.R;
import au.com.dius.resilience.loader.IncidentListLoader;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.persistence.repository.Repository;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;
import au.com.dius.resilience.ui.Themer;
import au.com.dius.resilience.ui.map.IncidentOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.inject.Inject;
import roboguice.activity.RoboMapActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.util.List;

import static au.com.dius.resilience.Constants.EXTRA_INCIDENT;
import static au.com.dius.resilience.Constants.EXTRA_PHOTO;

/**
 * @author georgepapas
 */
@ContentView(R.layout.activity_map_view)
public class MapViewActivity extends RoboMapActivity implements LoaderManager.LoaderCallbacks<List<Incident>> {

  public static final int ZOOM_LEVEL = 17;
  @InjectView(R.id.map_view)
  private MapView mapView;

  @InjectResource(R.drawable.blue_poi)
  private Drawable itemIcon;

  @Inject
  private Repository repository;

  @Inject
  private PreferenceAdapter preferenceAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Themer.applyCurrentTheme(this);
    super.onCreate(savedInstanceState);

    getLoaderManager().initLoader(IncidentListLoader.INCIDENT_LIST_LOADER, null, this);

    // TODO - When we have a nicer location service set up, hook this up properly.
    String lastKnownLatitude = (String) preferenceAdapter.getCommonPreference(R.string.last_known_latitude_key);
    String lastKnownLongtitude = (String) preferenceAdapter.getCommonPreference(R.string.last_known_longtitude_key);

    if (lastKnownLatitude == null || lastKnownLongtitude == null) {
      return;
    }

    mapView.setBuiltInZoomControls(true);
    mapView.getController().setCenter(new GeoPoint((int) (Double.parseDouble(lastKnownLatitude) * 1E6), (int) (Double.parseDouble(lastKnownLongtitude) * 1E6)));
    mapView.getController().setZoom(ZOOM_LEVEL);
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
    IncidentOverlay overlay = new IncidentOverlay(itemIcon, mapView, repository);
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
