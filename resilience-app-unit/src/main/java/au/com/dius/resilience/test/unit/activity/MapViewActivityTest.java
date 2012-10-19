package au.com.dius.resilience.test.unit.activity;

import au.com.dius.resilience.R;
import au.com.dius.resilience.ui.activity.MapViewActivity;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import roboguice.event.EventManager;
import roboguice.test.RobolectricRoboTestRunner;

/**
 * @author georgepapas
 */
@RunWith(RobolectricRoboTestRunner.class)
public class MapViewActivityTest {

  private MapActivity mapActivity;

  private MapView mapView;

  @Mock
  private EventManager mockEventManager;

  @Before
  public void setup() {
//    MockitoAnnotations.initMocks(this);
//
//    RoboGuice.setBaseApplicationInjector(
//            Robolectric.application,
//            Stage.DEVELOPMENT,
//            Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application)).with(new TestModule()));

//    mapActivity = new MapViewActivity();
//    mapActivity.setContentView(R.layout.activity_map_view);
//
//    mapView = (MapView) mapActivity.findViewById(R.id.map_view);
  }

  @Test
  public void zoomControllerShouldBeVisible() {
//    assertTrue(mapView.getZoomButtonsController().isVisible());
  }

}
