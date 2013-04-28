package au.com.dius.resilience.location.criteria;

import android.location.Location;
import au.com.dius.resilience.location.LocationCriteria;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class IsMoreAccurateCriteria implements LocationCriteria {

  public static final float SIGNIFICANT_ACCURACY_DIFFERENCE_METRES = 500.0f;

  @Override
  public boolean passes(Location candidateLocation, Location previousLocation) {

    if (previousLocation == null && candidateLocation != null) {
      return true;
    }

    return candidateLocation != null && isSignificantlyMoreAccurateThan(candidateLocation, previousLocation);
  }

  private boolean isSignificantlyMoreAccurateThan(Location candidateLocation, Location previousLocation) {
    float accuracyDifference = previousLocation.getAccuracy() - candidateLocation.getAccuracy();

    // if accuracyDifference SIGNIFICANT_ACCURACY_DIFFERENCE_METRES, then candidate is at least as accurate as best
    // if accuracyDifference > SIGNIFICANT_ACCURACY_DIFFERENCE_METRES then candidate is more accurate
    // if accuracyDifference < SIGNIFICANT_ACCURACY_DIFFERENCE_METRES then candidate is less accurate than best

    return accuracyDifference >= SIGNIFICANT_ACCURACY_DIFFERENCE_METRES;
  }
}
