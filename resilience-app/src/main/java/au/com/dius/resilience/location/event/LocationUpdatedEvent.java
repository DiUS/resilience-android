package au.com.dius.resilience.location.event;

import android.location.Location;

public class LocationUpdatedEvent {

  private Location location;

  public LocationUpdatedEvent(Location location) {
    this.location = location;
  }

  public Location getLocation() {
    return location;
  }
}
