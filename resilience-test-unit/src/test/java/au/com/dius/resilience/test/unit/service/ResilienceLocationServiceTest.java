package au.com.dius.resilience.test.unit.service;

import au.com.dius.resilience.service.ResilienceLocationService;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import roboguice.inject.ContextSingleton;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ResilienceLocationServiceTest {

  private ResilienceLocationService locationService;

  @Before
  public void setup() {
    locationService = new ResilienceLocationService();
  }

}
