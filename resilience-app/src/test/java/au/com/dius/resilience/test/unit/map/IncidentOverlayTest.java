package au.com.dius.resilience.test.unit.map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Point;
import au.com.dius.resilience.ui.map.IncidentOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class IncidentOverlayTest {

  IncidentOverlay overlay;

  @Mock
  private Context context;

  @Mock
  private Incident incident;

  @Mock
  private Drawable drawable;
  private OverlayItem overlayItem;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    MapView mapView = new MapView(context, "");
    overlay = new IncidentOverlay(drawable, mapView, null);
    overlayItem = new OverlayItem(new GeoPoint(0, 0), "", "");
  }

  @Test
  public void shouldIncrementSizeWhenOverlayAdded() {
    assertThat(overlay.size(), is(0));
    overlay.addOverlay(overlayItem);
    assertThat(overlay.size(), is(1));
  }

  @Test
  public void createItemShouldReturnAddedOverlays() {
    overlay.addOverlay(overlayItem);
    OverlayItem item = overlay.createItem(0);
    assertSame(overlayItem, item);
  }

  @Test
  public void shouldHaveItemsWhenOverlayIsAdded() {
    assertThat(overlay.hasItems(), is(false));
    overlay.addOverlay(overlayItem);
    assertThat(overlay.hasItems(), is(true));
  }

  @Test
  public void shouldPopulateOverlayIfIncidentHasGeoData() {
    ArrayList<Incident> incidents = new ArrayList<Incident>();

    when(incident.getPoint()).thenReturn(new Point(1,1));
    incidents.add(incident);

    overlay.populateWith(incidents);

    assertThat(overlay.size(), is(1));
  }

  @Test
  public void shouldNotPopulateOverlayIfIncidentLacksGeoData() {
    ArrayList<Incident> incidents = new ArrayList<Incident>();

    when(incident.getPoint()).thenReturn(null);
    incidents.add(incident);

    overlay.populateWith(incidents);

    assertThat(overlay.size(), is(0));
  }
}
