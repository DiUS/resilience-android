package au.com.dius.resilience.location.criteria;

import android.location.Location;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static au.com.dius.resilience.location.criteria.IsMoreAccurateCriteria.SIGNIFICANT_ACCURACY_DIFFERENCE_METRES;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(ResilienceTestRunner.class)
public class IsMoreAccurateCriteriaTest {

  private IsMoreAccurateCriteria moreAccurateCriteria;

  @Mock
  private Location bestLocation;

  @Mock
  private Location candidateLocation;

  @Before
  public void setup() {
    moreAccurateCriteria = new IsMoreAccurateCriteria();
  }

  @Test
  public void shouldPassIfCandidateLocationIsMoreAccurateThanBest() {
    given(bestLocation.getAccuracy()).willReturn(600.0f);
    given(candidateLocation.getAccuracy()).willReturn(600.0f - SIGNIFICANT_ACCURACY_DIFFERENCE_METRES);
    assertThat(moreAccurateCriteria.passes(candidateLocation, bestLocation), is(true));
  }

  @Test
  public void shouldFailIfCandidateLocationIsLessAccurateThanBest() {
    given(bestLocation.getAccuracy()).willReturn(600.0f);
    given(candidateLocation.getAccuracy()).willReturn(600.0f - SIGNIFICANT_ACCURACY_DIFFERENCE_METRES + 1L);
    assertThat(moreAccurateCriteria.passes(candidateLocation, bestLocation), is(false));
  }

  @Test
  public void shouldFailIfCandidateLocationIsNull() {
    IsMoreAccurateCriteria moreAccurateCriteriaNullCandidate = new IsMoreAccurateCriteria();
    assertThat(moreAccurateCriteriaNullCandidate.passes(null, bestLocation), is(false));
  }

  @Test
  public void shouldPassIfBestIsNullAndCandidateIsNot() {
    IsMoreAccurateCriteria moreAccurateCriteriaNullBest = new IsMoreAccurateCriteria();
    assertThat(moreAccurateCriteriaNullBest.passes(candidateLocation, null), is(true));
  }

  @Test
  public void shouldFailIfBothAreNull() {
    IsMoreAccurateCriteria moreAccurateCriteriaBothNull = new IsMoreAccurateCriteria();
    assertThat(moreAccurateCriteriaBothNull.passes(null, null), is(false));
  }
}