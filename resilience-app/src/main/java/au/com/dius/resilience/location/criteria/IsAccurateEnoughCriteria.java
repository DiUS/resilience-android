package au.com.dius.resilience.location.criteria;

import android.location.Location;
import au.com.dius.resilience.location.LocationCriteria;

public class IsAccurateEnoughCriteria implements LocationCriteria {

  public static final float MIN_ACCURACY_METRES = 1000;

  @Override
  public boolean passes(Location candidateLocation, Location previousLocation) {
    return candidateLocation != null && candidateLocation.getAccuracy() <= MIN_ACCURACY_METRES;
  }
}