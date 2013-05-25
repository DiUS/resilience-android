package au.com.dius.resilience.test.unit.utils;

import au.com.dius.resilience.shadow.ResilienceShadowActivity;
import au.com.dius.resilience.test.unit.fragment.ResilienceShadowFragment;
import au.com.dius.resilience.test.unit.loader.ShadowLoader;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.bytecode.ShadowWrangler;
import org.junit.runners.model.InitializationError;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;

/**
 * @author georgepapas
 */
public class ResilienceTestRunner extends RobolectricTestRunner {

  public ResilienceTestRunner(Class testClass) throws InitializationError {
    super(testClass);

    ShadowWrangler.getInstance().debug = false;
  }

  @Override
  public void prepareTest(Object test) {
    super.prepareTest(test);
    MockitoAnnotations.initMocks(test);
  }

  @Override
  public void beforeTest(Method method) {
    Robolectric.bindShadowClass(SameDatabaseShadowSqlLiteOpenHelper.class);
    Robolectric.bindShadowClass(ShadowLoader.class);
    Robolectric.bindShadowClass(ResilienceShadowFragment.class);
    Robolectric.bindShadowClass(ResilienceShadowActivity.class);
  }
}
