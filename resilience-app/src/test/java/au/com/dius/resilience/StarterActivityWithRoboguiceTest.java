package au.com.dius.resilience;

import android.widget.TextView;
import au.com.dius.resilience.test.support.FakeCurrentTime;
import au.com.dius.resilience.test.support.RobolectricTestRunnerWithInjection;
import au.com.dius.resilience.util.CurrentTime;
import com.google.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class StarterActivityWithRoboguiceTest {
    @Inject
    CurrentTime currentTime;

    @Test
    public void testCurrentTimeIsInjected() throws Exception {
        ((FakeCurrentTime) currentTime).setCurrentTime(12345L);
        final StarterActivity activity = new StarterActivity();
        activity.onCreate(null);
        TextView title = (TextView) activity.findViewById(R.id.current_time);
        assertEquals("12345", title.getText());
    }
}
