package au.com.dius.resilience.test.support;

import au.com.dius.resilience.ApplicationModule;
import au.com.dius.resilience.MySampleApplication;
import au.com.dius.resilience.util.CurrentTime;
import org.junit.runners.model.InitializationError;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import roboguice.RoboGuice;

public class RobolectricTestRunnerWithInjection extends RobolectricTestRunner {

    public RobolectricTestRunnerWithInjection(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    public void prepareTest(Object test) {
        MySampleApplication application = (MySampleApplication) Robolectric.application;


        RoboGuice.setBaseApplicationInjector(application, RoboGuice.DEFAULT_STAGE,
                RoboGuice.newDefaultRoboModule(application), new TestApplicationModule());

        RoboGuice.getInjector(application).injectMembers(test);
        MockitoAnnotations.initMocks(test);
    }

    public static class TestApplicationModule extends ApplicationModule {
        @Override
        public void configure() {
            bind(CurrentTime.class).toInstance(new FakeCurrentTime());

            super.configure();
        }
    }

}
