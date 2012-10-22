package au.com.dius.resilience.ui.activity;

import android.os.Bundle;
import android.widget.Toast;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Point;
import com.google.android.maps.MapView;
import roboguice.activity.RoboMapActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author georgepapas
 */
@ContentView(R.layout.activity_map_view)
public class MapViewActivity extends RoboMapActivity {

  @InjectView(R.id.map_view)
  private MapView mapView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

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
