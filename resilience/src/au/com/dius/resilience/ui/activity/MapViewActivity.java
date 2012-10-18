package au.com.dius.resilience.ui.activity;

import android.os.Bundle;
import android.view.View;
import au.com.dius.resilience.R;
import com.google.android.maps.MapView;
import roboguice.activity.RoboMapActivity;
import roboguice.inject.InjectView;

/**
 * @author georgepapas
 */
public class MapViewActivity extends RoboMapActivity {

  @InjectView(R.id.fragment_incident_map_view)
  private MapView contentView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    contentView.setBuiltInZoomControls(true);
  }

  @Override
  protected boolean isRouteDisplayed() {
    return false;
  }
}
