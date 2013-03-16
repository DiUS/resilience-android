package au.com.dius.resilience.test.support;

import au.com.dius.resilience.util.CurrentTime;

public class FakeCurrentTime extends CurrentTime {
    private long fakeTime;

    @Override
    public long currentTimeMillis() {
        return fakeTime;
    }

    public void setCurrentTime(long time) {
        fakeTime = time;
    }
}
