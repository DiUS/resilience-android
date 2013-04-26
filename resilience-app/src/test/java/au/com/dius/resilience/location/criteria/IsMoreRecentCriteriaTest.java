package au.com.dius.resilience.location.criteria;

import android.location.Location;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(ResilienceTestRunner.class)
public class IsMoreRecentCriteriaTest {

  private IsMoreRecentCriteria isMoreRecentCriteria;

  @Mock
  private Location bestLocation;

  @Mock
  private Location candidateLocation;

  private final long NOW = System.currentTimeMillis();

  @Before
  public void setup() {
    isMoreRecentCriteria = new IsMoreRecentCriteria();
  }

  @Test
  public void shouldPassIfCandidateLocationIsMoreRecentThanBest() {
    given(bestLocation.getTime()).willReturn(NOW);
    given(candidateLocation.getTime()).willReturn(NOW - 100L);
    assertThat(isMoreRecentCriteria.passes(candidateLocation, bestLocation), is(true));
  }

  @Test
  public void shouldFailIfCandidateLocationIsLessRecentThanBest() {
    given(bestLocation.getTime()).willReturn(NOW);
    given(candidateLocation.getTime()).willReturn(NOW + 100L);
    assertThat(isMoreRecentCriteria.passes(candidateLocation, bestLocation), is(false));
  }
}