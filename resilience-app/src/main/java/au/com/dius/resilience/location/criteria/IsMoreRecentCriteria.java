package au.com.dius.resilience.location.criteria;

import android.location.Location;
import au.com.dius.resilience.location.LocationCriteria;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class IsMoreRecentCriteria implements LocationCriteria {

  public static final long SIGNIFICANT_AGE_DIFFERENCE_MS = 1000 * 60; // One minute

  @Override
  public boolean passes(Location candidateLocation, Location previousLocation) {
    return isSignificantlyOlderThan(candidateLocation, previousLocation);
  }

  private boolean isSignificantlyOlderThan(Location candidateLocation, Location previousLocation) {
    return candidateLocation.getTime() - previousLocation.getTime() >= SIGNIFICANT_AGE_DIFFERENCE_MS;
  }
}
