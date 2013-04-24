package au.com.dius.resilience.location;

import com.google.inject.Inject;

public class StopLocatingRunnable implements Runnable {

  @Inject
  private LocationBroadcaster locationBroadcaster;

  @Override
  public void run() {
    locationBroadcaster.stopPolling();
  }
}
