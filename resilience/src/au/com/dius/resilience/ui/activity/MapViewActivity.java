package au.com.dius.resilience.ui.activity;

import android.os.Bundle;
import au.com.dius.resilience.R;
import com.google.android.maps.MapView;
import roboguice.activity.RoboMapActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author georgepapas
 */
@ContentView(R.layout.fragment_map_view)
public class MapViewActivity extends RoboMapActivity {

  @InjectView(R.id.map_view)
  private MapView mapView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mapView.setBuiltInZoomControls(true);
  }

  @Override
  protected boolean isRouteDisplayed() {
    return false;
  }
}
