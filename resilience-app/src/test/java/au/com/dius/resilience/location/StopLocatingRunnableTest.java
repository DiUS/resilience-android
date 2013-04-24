package au.com.dius.resilience.location;

import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import au.com.dius.resilience.test.unit.utils.TestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;

@RunWith(ResilienceTestRunner.class)
public class StopLocatingRunnableTest {

  private StopLocatingRunnable stopLocatingRunnable;

  @Mock
  private LocationBroadcaster locationBroadcaster;

  @Before
  public void setup() {
    stopLocatingRunnable = new StopLocatingRunnable();
    TestHelper.setField(stopLocatingRunnable, "locationBroadcaster", locationBroadcaster);
  }

  @Test
  public void shouldStopPollingWhenRun() {
    stopLocatingRunnable.run();
    verify(locationBroadcaster).stopPolling();
  }
}
