package au.com.dius.resilience.location.criteria;

import android.location.Location;
import au.com.dius.resilience.location.LocationCriteria;

public class IsMoreRecentCriteria implements LocationCriteria {

  @Override
  public boolean passes(Location candidateLocation, Location previousLocation) {
    return candidateLocation.getTime() < previousLocation.getTime();
  }
}
