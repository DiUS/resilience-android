package au.com.dius.resilience.test.unit.map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Point;
import au.com.dius.resilience.ui.map.ResilienceItemisedOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class ResilienceItemisedOverlayTest {

  ResilienceItemisedOverlay overlay;

  @Mock
  private Context context;

  @Mock
  private Drawable drawable;
  private OverlayItem overlayItem;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    MapView mapView = new MapView(context, "");
    overlay = new ResilienceItemisedOverlay(drawable, mapView);
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
    Incident incident1 = mock(Incident.class);
    when(incident1.getPoint()).thenReturn(new Point(1,1));
    incidents.add(incident1);

    overlay.populateWith(incidents);

    assertThat(overlay.size(), is(1));
  }

  @Test
  public void shouldNotPopulateOverlayIfIncidentLacksGeoData() {
    ArrayList<Incident> incidents = new ArrayList<Incident>();
    Incident incident1 = mock(Incident.class);
    when(incident1.getPoint()).thenReturn(null);
    incidents.add(incident1);

    overlay.populateWith(incidents);

    assertThat(overlay.size(), is(0));
  }
}
