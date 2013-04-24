package au.com.dius.resilience.test.unit.location;

import au.com.dius.resilience.location.ScheduledExecutorFactory;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ScheduledExecutorService;

import static junit.framework.Assert.assertNotNull;

@RunWith(ResilienceTestRunner.class)
public class ScheduledExecutorFactoryTest {

  private ScheduledExecutorFactory scheduledExecutorFactory;

  @Before
  public void setup() {
    scheduledExecutorFactory = new ScheduledExecutorFactory();
  }

  @Test
  public void shouldCreateScheduledExecutor() {
    ScheduledExecutorService scheduledExecutor = scheduledExecutorFactory.createScheduledExecutor();
    assertNotNull(scheduledExecutor);
  }
}
