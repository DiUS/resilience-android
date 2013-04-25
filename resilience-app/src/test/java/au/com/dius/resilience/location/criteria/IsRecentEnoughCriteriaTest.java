package au.com.dius.resilience.location.criteria;

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
public class IsRecentEnoughCriteriaTest {

  private IsRecentEnoughCriteria recentEnoughCriteria;

  @Mock
  private Location location;

  @Mock
  private TimeFactory timeFactory;

  private long NOW = System.currentTimeMillis();

  @Before
  public void setup() {
    when(timeFactory.currentTimeMillis()).thenReturn(NOW);
    recentEnoughCriteria = new IsRecentEnoughCriteria(location);
    setField(recentEnoughCriteria, "timeFactory", timeFactory);
  }

  @Test
  public void shouldPassIfLocationIsNewEnough() {
    given(location.getTime()).willReturn(NOW + IsRecentEnoughCriteria.MIN_AGE - 1L);
    assertThat(recentEnoughCriteria.passes(), is(true));
  }

  @Test
  public void shouldFailIfLocationIsTooOld() {
    given(location.getTime()).willReturn(NOW + IsRecentEnoughCriteria.MIN_AGE + 1L);
    assertThat(recentEnoughCriteria.passes(), is(false));
  }

  @Test
  public void shouldFailIfLocationIsNull() {
    IsRecentEnoughCriteria recentEnoughCriteriaNullLocation = new IsRecentEnoughCriteria(null);
    assertThat(recentEnoughCriteriaNullLocation.passes(), is(false));
  }
}