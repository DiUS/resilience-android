package au.com.dius.resilience;

import com.xtremelabs.robolectric.RobolectricConfig;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Ignore;
import org.junit.runners.model.InitializationError;

import java.io.File;

/**
 * @author georgepapas
 */
@Ignore
public class ResilienceRobolectricTestRunner extends RobolectricTestRunner {
  public static final String MAIN_PROJECT_PATH =
          new File(".").getAbsolutePath() + System.getProperty("file.separator") + "resilience";

  public ResilienceRobolectricTestRunner(Class<?> testClass) throws InitializationError {
    super(testClass, new RobolectricConfig(new File(MAIN_PROJECT_PATH)));
  }
}
