package au.com.dius.resilience.location;

import roboguice.inject.ContextSingleton;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/*
 * Factory for creating ScheduledExecutorServices.
 * This is pretty much here so I can test the app properly.
 */
@ContextSingleton
public class ScheduledExecutorFactory {

  public ScheduledExecutorService createScheduledExecutor() {
   return Executors.newScheduledThreadPool(1);
  }
}
