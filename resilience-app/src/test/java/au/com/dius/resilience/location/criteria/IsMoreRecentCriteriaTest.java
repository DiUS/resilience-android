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
  private Location previousLocation;

  @Mock
  private Location candidateLocation;

  private final long NOW = System.currentTimeMillis();

  @Before
  public void setup() {
    isMoreRecentCriteria = new IsMoreRecentCriteria();
  }

  @Test
  public void shouldPassIfCandidateLocationIsMoreRecentThanPrevious() {
    given(previousLocation.getTime()).willReturn(NOW);
    given(candidateLocation.getTime()).willReturn(NOW + IsMoreRecentCriteria.SIGNIFICANT_AGE_DIFFERENCE_MS);
    assertThat(isMoreRecentCriteria.passes(candidateLocation, previousLocation), is(true));
  }

  @Test
  public void shouldFailIfCandidateLocationIsLessRecentThanBest() {
    given(previousLocation.getTime()).willReturn(NOW);
    given(candidateLocation.getTime()).willReturn(NOW + IsMoreRecentCriteria.SIGNIFICANT_AGE_DIFFERENCE_MS - 1L);
    assertThat(isMoreRecentCriteria.passes(candidateLocation, previousLocation), is(false));
  }

  @Test
  public void shouldPassIfPreviousLocationIsNullAndCandidateIsNot() {
    assertThat(isMoreRecentCriteria.passes(candidateLocation, null), is(true));
  }

  @Test
  public void shouldFailIfPreviousAndCandidateLocationAreNull() {
    assertThat(isMoreRecentCriteria.passes(null, null), is(false));
  }

  @Test
  public void shouldFailIfCandidateLocationIsNullAndPreviousIsNot() {
    assertThat(isMoreRecentCriteria.passes(null, previousLocation), is(false));
  }
}