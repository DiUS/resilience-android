package au.com.dius.resilience.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Point;
import au.com.dius.resilience.ui.map.ResilienceItemisedOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import roboguice.activity.RoboMapActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import java.util.List;

/**
 * @author georgepapas
 */
@ContentView(R.layout.activity_map_view)
public class MapViewActivity extends RoboMapActivity {

  List<Overlay> mapOverlays;

  @InjectView(R.id.map_view)
  private MapView mapView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mapOverlays = mapView.getOverlays();
    Drawable drawable = this.getResources().getDrawable(R.drawable.create_incident);
    ResilienceItemisedOverlay itemisedOverlay = new ResilienceItemisedOverlay(this, drawable);

    GeoPoint geoPoint = new GeoPoint(19240000, -99120000);
    OverlayItem overlayItem = new OverlayItem(geoPoint, "Mexico City, yay!", "I'm im Mexico city pals!");

    itemisedOverlay.addOverlay(overlayItem);
    mapOverlays.add(itemisedOverlay);

    mapView.setBuiltInZoomControls(true);

    final Point point = (Point) getIntent().getSerializableExtra(Constants.INCIDENT_POINT);
    if (point != null) {
      Toast.makeText(this, "Map Coordinates are " + point.getLatitude() + " , " + point.getLongitude(), Toast.LENGTH_LONG).show();
    }
  }

  @Override
  protected boolean isRouteDisplayed() {
    return false;
  }
}
