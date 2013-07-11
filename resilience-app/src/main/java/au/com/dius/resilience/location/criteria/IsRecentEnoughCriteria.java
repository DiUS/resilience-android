package au.com.dius.resilience.location.criteria;

import android.location.Location;
import au.com.dius.resilience.factory.TimeFactory;
import au.com.dius.resilience.location.LocationCriteria;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class IsRecentEnoughCriteria implements LocationCriteria {

  public static final long MIN_AGE = 1000 * 60 * 5; // 5 Minutes

  @Inject
  private TimeFactory timeFactory;

  @Override
  public boolean passes(Location candidateLocation, Location previousLocation) {

    if (candidateLocation == null) {
      return false;
    }

    long locationAge = candidateLocation.getTime() - timeFactory.currentTimeMillis();
    return locationAge <= MIN_AGE;
  }
}
