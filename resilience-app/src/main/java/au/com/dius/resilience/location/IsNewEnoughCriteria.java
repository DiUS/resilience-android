package au.com.dius.resilience.location;

import android.location.Location;
import au.com.dius.resilience.factory.TimeFactory;
import com.google.inject.Inject;

public class IsNewEnoughCriteria implements LocationCriteria {

  public static final long MIN_AGE = 1000 * 60 * 2; // Two minutes

  private final Location location;

  @Inject
  private TimeFactory timeFactory;

  public IsNewEnoughCriteria(Location aLocation) {
    location = aLocation;
  }

  @Override
  public boolean passes() {

    if (location == null) {
      return false;
    }

    long locationAge = location.getTime() - timeFactory.currentTimeMillis();
    return locationAge <= MIN_AGE;
  }
}
