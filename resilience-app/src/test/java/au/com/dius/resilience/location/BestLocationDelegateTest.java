package au.com.dius.resilience.location;

import android.location.Location;
import android.location.LocationManager;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import au.com.dius.resilience.test.unit.utils.TestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(ResilienceTestRunner.class)
public class BestLocationDelegateTest {

  private BestLocationDelegate bestLocationDelegate;

  @Mock
  private Location candidateLocation;

  @Mock
  private Location previousLocation;

  @Mock
  private LocationCriteria passesCriteria;

  @Mock
  private LocationCriteria failsCriteria;

  @Mock
  private LocationManager locationManager;

  @Before
  public void setup() {
    bestLocationDelegate = new BestLocationDelegate();

    List<String> providers = new ArrayList<String>();
    when(locationManager.getAllProviders()).thenReturn(providers);
    TestHelper.setField(bestLocationDelegate, "locationManager", locationManager);

    when(passesCriteria.passes(any(Location.class), any(Location.class))).thenReturn(true);
    when(failsCriteria.passes(any(Location.class), any(Location.class))).thenReturn(false);

    List<LocationCriteria> criteria = new ArrayList<LocationCriteria>();
    TestHelper.setField(bestLocationDelegate, "criteria", criteria);
  }

  @Test
  public void shouldReturnCandidateLocationWhenAllCriteriaPass() {
    addCriteria(passesCriteria);

    mockAccurateLocation(locationManager);
    Location location = bestLocationDelegate.getBestLastKnownLocation();
    assertSame(location, candidateLocation);
  }

  @Test
  public void shouldReturnNullLocationWhenACriteriaFailsAndNoBestExists() {
    addCriteria(failsCriteria);

    Location location = bestLocationDelegate.getBestLastKnownLocation();
    assertNull(location);
    assertNotSame(location, candidateLocation);
  }

  @Test
  public void shouldReturnPreviousGoodLocationIfACriteriaFailsForCandidate() {
    LocationCriteria failsForCandidateCriteria = mock(LocationCriteria.class);

    mockLessAccurateLocation(locationManager);
    mockAccurateLocation(locationManager);

    addCriteria(failsForCandidateCriteria);

    when(failsForCandidateCriteria.passes(eq(previousLocation), any(Location.class))).thenReturn(true);
    when(failsForCandidateCriteria.passes(candidateLocation, previousLocation)).thenReturn(false);

    Location bestLastKnownLocation = bestLocationDelegate.getBestLastKnownLocation();

    assertSame(previousLocation, bestLastKnownLocation);
  }

  private void mockLessAccurateLocation(LocationManager locationManager) {
    locationManager.getAllProviders().add(LocationManager.NETWORK_PROVIDER);
    when(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)).thenReturn(previousLocation);
    TestHelper.setField(bestLocationDelegate, "locationManager", locationManager);
  }

  private void mockAccurateLocation(LocationManager locationManager) {
    locationManager.getAllProviders().add(LocationManager.GPS_PROVIDER);
    when(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)).thenReturn(candidateLocation);
    TestHelper.setField(bestLocationDelegate, "locationManager", locationManager);
  }

  private void addCriteria(LocationCriteria locationCriteria) {
    List<LocationCriteria> criteria
      = (List<LocationCriteria>) TestHelper.getField(bestLocationDelegate, "criteria");
    criteria.add(locationCriteria);
  }
}