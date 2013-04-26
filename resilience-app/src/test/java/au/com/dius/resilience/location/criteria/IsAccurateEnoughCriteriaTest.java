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
public class IsAccurateEnoughCriteriaTest {

  private IsAccurateEnoughCriteria isAccurateEnoughCriteria;

  @Mock
  private Location location;

  @Before
  public void setup() {
    isAccurateEnoughCriteria = new IsAccurateEnoughCriteria();
  }

  @Test
  public void shouldPassIfLocationAccurateEnough() {
    given(location.getAccuracy()).willReturn(IsAccurateEnoughCriteria.MIN_ACCURACY_METRES);
    assertThat(isAccurateEnoughCriteria.passes(location, null), is(true));
  }

  @Test
  public void shouldFailIfLocationNotAccurateEnough() {
    given(location.getAccuracy()).willReturn(IsAccurateEnoughCriteria.MIN_ACCURACY_METRES + 1L);
    assertThat(isAccurateEnoughCriteria.passes(location, null), is(false));
  }

  @Test
  public void shouldFailIfLocationIsNull() {
    IsAccurateEnoughCriteria isAccurateEnoughCriteriaNullLocation = new IsAccurateEnoughCriteria();
    assertThat(isAccurateEnoughCriteriaNullLocation.passes(null, null), is(false));
  }
}
