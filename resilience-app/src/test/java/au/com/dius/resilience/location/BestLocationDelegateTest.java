package au.com.dius.resilience.location;

import android.location.Location;
import au.com.dius.resilience.factory.TimeFactory;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static au.com.dius.resilience.test.unit.utils.TestHelper.setField;
import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.when;

@RunWith(ResilienceTestRunner.class)
public class BestLocationDelegateTest {

  private BestLocationDelegate bestLocationDelegate;

  @Mock
  private Location bestLocation;

  @Mock
  private Location candidateLocation;

  @Mock
  private TimeFactory timeFactory;

  private final long NOW = System.currentTimeMillis();

  @Before
  public void setup() {
    bestLocationDelegate = new BestLocationDelegate();
    when(timeFactory.currentTimeMillis()).thenReturn(NOW);

    setField(bestLocationDelegate, "timeFactory", timeFactory);
  }

  @Test
  public void shouldReturnBestLocationWhenCandidateIsNull() {
    Location location = bestLocationDelegate.bestLocationOf(null, bestLocation);
    assertSame(location, bestLocation);
  }

//  @Test
//  public void shouldReturnBestLocationWhenCandidateIsTooOld() {
//    given(candidateLocation.getTime()).willReturn(NOW + BestLocationDelegate.MIN_AGE +1L);
//    Location location = bestLocationDelegate.bestLocationOf(candidateLocation, bestLocation);
//    assertSame(location, bestLocation);
//  }
}
