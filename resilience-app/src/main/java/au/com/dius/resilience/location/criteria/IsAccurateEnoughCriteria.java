package au.com.dius.resilience.location.criteria;

import android.location.Location;
import au.com.dius.resilience.location.LocationCriteria;

public class IsAccurateEnoughCriteria implements LocationCriteria {

  public static final float MIN_ACCURACY_METRES = 1000;

  private final Location location;

  public IsAccurateEnoughCriteria(Location aLocation) {
    location = aLocation;
  }

  @Override
  public boolean passes() {
    return location != null && location.getAccuracy() <= MIN_ACCURACY_METRES;
  }
}