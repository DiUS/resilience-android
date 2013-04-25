package au.com.dius.resilience.location;

import android.location.Location;
import au.com.dius.resilience.factory.TimeFactory;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static au.com.dius.resilience.test.unit.utils.TestHelper.setField;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(ResilienceTestRunner.class)
public class IsNewEnoughCriteriaTest {

  private IsNewEnoughCriteria isNewEnoughCriteria;

  @Mock
  private Location location;

  @Mock
  private TimeFactory timeFactory;

  private long NOW = System.currentTimeMillis();

  @Before
  public void setup() {
    when(timeFactory.currentTimeMillis()).thenReturn(NOW);
    isNewEnoughCriteria = new IsNewEnoughCriteria(location);
    setField(isNewEnoughCriteria, "timeFactory", timeFactory);
  }

  @Test
  public void shouldPassIfLocationIsNewEnough() {
    given(location.getTime()).willReturn(NOW + IsNewEnoughCriteria.MIN_AGE - 1L);
    assertThat(isNewEnoughCriteria.passes(), is(true));
  }

  @Test
  public void shouldFailIfLocationIsTooOld() {
    given(location.getTime()).willReturn(NOW + IsNewEnoughCriteria.MIN_AGE + 1L);
    assertThat(isNewEnoughCriteria.passes(), is(false));
  }

  @Test
  public void shouldFailIfLocationIsNull() {
    IsNewEnoughCriteria isNewEnoughCriteriaNullLocation = new IsNewEnoughCriteria(null);
    assertThat(isNewEnoughCriteriaNullLocation.passes(), is(false));
  }
}