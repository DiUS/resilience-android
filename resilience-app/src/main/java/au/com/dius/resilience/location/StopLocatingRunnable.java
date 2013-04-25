package au.com.dius.resilience.location;

public class StopLocatingRunnable implements Runnable {

  private LocationBroadcaster locationBroadcaster;

  public StopLocatingRunnable(LocationBroadcaster aLocationBroadcaster) {
    locationBroadcaster = aLocationBroadcaster;
  }

  @Override
  public void run() {
    locationBroadcaster.stopPolling();
  }
}
