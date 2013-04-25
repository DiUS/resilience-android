package au.com.dius.resilience.factory;

import roboguice.inject.ContextSingleton;

/*
 * Factory for getting the system time. Having this factory
 * will make testing a bit easier.
 */
@ContextSingleton
public class TimeFactory {

  public long currentTimeMillis() {
    return System.currentTimeMillis();
  }
}
