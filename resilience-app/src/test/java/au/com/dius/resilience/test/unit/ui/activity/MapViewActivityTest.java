package au.com.dius.resilience.test.unit.ui.activity;

import android.content.Loader;
import au.com.dius.resilience.loader.ServiceRequestLoader;
import au.com.dius.resilience.ui.activity.MapViewActivity;
import au.com.justinb.open311.model.ServiceRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import junitx.util.PrivateAccessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

// Need to use PowerMockRunner here since the GoogleMap class is declared final
// and therefore cannot be mocked with vanilla Mockito.
@RunWith(PowerMockRunner.class)
@PrepareForTest(GoogleMap.class)
public class MapViewActivityTest {

  public static final int UNSUPPORTED_ID = 5;

  @Mock
  private MapViewActivity mapViewActivity;
  @Mock
  private Loader serviceRequestLoader;
  @Mock
  private ServiceRequest serviceRequest1;
  @Mock
  private ServiceRequest serviceRequest2;
  @Mock
  private ServiceRequest serviceRequest3;
  @Mock
  private ServiceRequest serviceRequest4;
  @Mock
  private ServiceRequest serviceRequest5;

  private GoogleMap googleMap;

  private ArrayList<ServiceRequest> mixedData = new ArrayList<ServiceRequest>();
  private ArrayList<ServiceRequest> noData = new ArrayList<ServiceRequest>();
  private ArrayList<ServiceRequest> noLat = new ArrayList<ServiceRequest>();
  private ArrayList<ServiceRequest> noLong = new ArrayList<ServiceRequest>();
  private ArrayList<ServiceRequest> noLatOrLong = new ArrayList<ServiceRequest>();

  @Before
  public void setup() throws Exception {
    MockitoAnnotations.initMocks(this);

    // Since we can't (as far as I know) use Robolectric alongside Powermock, I'm mocking MapViewActivity
    // and using callReadMethod on methods I want to test. Otherwise a Stub! exception is thrown
    // form the MapViewActivity constructor.
    Mockito.doCallRealMethod().when(mapViewActivity).onLoadFinished(eq(serviceRequestLoader), anyList());
    Mockito.doCallRealMethod().when(mapViewActivity).onLoaderReset(null);
    googleMap = PowerMockito.mock(GoogleMap.class);

    PrivateAccessor.setField(mapViewActivity, "map", googleMap);

    // Has all required data
    when(serviceRequest1.getLat()).thenReturn(11.1);
    when(serviceRequest1.getLong()).thenReturn(22.1);
    when(serviceRequest2.getLat()).thenReturn(33.3);
    when(serviceRequest2.getLong()).thenReturn(44.4);
    // Missing longtitude
    when(serviceRequest3.getLat()).thenReturn(33.3);
    when(serviceRequest3.getLong()).thenReturn(null);
    noLong.add(serviceRequest3);
    // Missing latitude
    when(serviceRequest4.getLat()).thenReturn(null);
    when(serviceRequest4.getLong()).thenReturn(44.4);
    noLat.add(serviceRequest3);
    // Missing both lat and long
    when(serviceRequest5.getLat()).thenReturn(null);
    when(serviceRequest5.getLat()).thenReturn(null);
    noLatOrLong.add(serviceRequest5);

    mixedData.add(serviceRequest1);
    mixedData.add(serviceRequest2);
    mixedData.add(serviceRequest3);
    mixedData.add(serviceRequest4);
    mixedData.add(serviceRequest5);

    when(serviceRequestLoader.getId()).thenReturn(ServiceRequestLoader.SERVICE_REQUEST_LIST_LOADER);
  }

  @Test
  public void shouldNotAddMarkersForUnsupportedLoader() {
    when(serviceRequestLoader.getId()).thenReturn(UNSUPPORTED_ID);
    mapViewActivity.onLoadFinished(serviceRequestLoader, mixedData);
    verify(googleMap, never()).addMarker(any(MarkerOptions.class));
  }

  @Test
  public void shouldAddMarkersToMapOnServiceRequestLoad() {
    mapViewActivity.onLoadFinished(serviceRequestLoader, mixedData);
    verify(googleMap, times(2)).addMarker(any(MarkerOptions.class));
  }

  @Test
  public void shouldNotAddMarkersWhenNoDataReturned() {
    mapViewActivity.onLoadFinished(serviceRequestLoader, noData);
    verify(googleMap, never()).addMarker(any(MarkerOptions.class));
  }

  @Test
  public void shouldNotAddMarkerWithMissingLatitude() {
    mapViewActivity.onLoadFinished(serviceRequestLoader, noLat);
    verify(googleMap, never()).addMarker(any(MarkerOptions.class));
  }

  @Test
  public void shouldNotAddMarkerWithMissingLongtitude() {
    mapViewActivity.onLoadFinished(serviceRequestLoader, noLong);
    verify(googleMap, never()).addMarker(any(MarkerOptions.class));
  }

  @Test
  public void shouldNotAddMarkerWithMissingLongtitudeAndLatitude() {
    mapViewActivity.onLoadFinished(serviceRequestLoader, noLatOrLong);
    verify(googleMap, never()).addMarker(any(MarkerOptions.class));
  }
  
  @Test
  public void mapClearedOnLoaderReset() {
    mapViewActivity.onLoaderReset(null);
    verify(googleMap).clear();
  }
}
