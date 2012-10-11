package au.com.dius.resilience.persistence;

import android.content.Context;
import au.com.dius.resilience.RuntimeProperties;

public class RepositoryFactory {

  private static boolean isTest = false;
  
  public static Repository create(Context context) {
    if (!RuntimeProperties.useLiveDb()) {
      return new SqlLiteRepository(context);
    }
    
    return new ParseRepository();
  }
  
  public static void setTestFlag(boolean test) {
    isTest = test;
  }
}
