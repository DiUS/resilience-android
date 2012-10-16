package au.com.dius.resilience.utils;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.Method;

/**
 * @author georgepapas
 */
public class SameDatabaseCustomTestRunner extends RobolectricTestRunner {

  public SameDatabaseCustomTestRunner(Class testClass) throws InitializationError {
       super(testClass);
   }

   @Override public void beforeTest(Method method) {
       Robolectric.bindShadowClass(SameDatabaseShadowSqlLiteOpenHelper.class);
   }
}
