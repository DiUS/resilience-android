package au.com.dius.resilience.location;

import android.location.Location;

public interface LocationCriteria {

  boolean passes(Location candidateLocation, Location previousLocation);
}
