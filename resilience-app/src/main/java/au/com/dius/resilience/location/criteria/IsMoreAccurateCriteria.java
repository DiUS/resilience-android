package au.com.dius.resilience.location.criteria;

import android.location.Location;
import au.com.dius.resilience.location.LocationCriteria;

public class IsMoreAccurateCriteria implements LocationCriteria {

  private final Location bestLocation;
  private final Location candidateLocation;

  public static final float SIGNIFICANT_ACCURACY_DIFFERENCE_METRES = 500.0f;

  public IsMoreAccurateCriteria(Location aBestLocation, Location aCandidateLocation) {
    bestLocation = aBestLocation;
    candidateLocation = aCandidateLocation;
  }

  @Override
  public boolean passes() {

    if (bestLocation == null && candidateLocation != null) {
      return true;
    }

    return candidateLocation != null && isSignificantlyMoreAccurateThan(bestLocation, candidateLocation);
  }

  private boolean isSignificantlyMoreAccurateThan(Location bestLocation, Location candidateLocation) {
    float accuracyDifference = bestLocation.getAccuracy() - candidateLocation.getAccuracy();

    // if accuracyDifference SIGNIFICANT_ACCURACY_DIFFERENCE_METRES, then candidate is at least as accurate as best
    // if accuracyDifference > SIGNIFICANT_ACCURACY_DIFFERENCE_METRES then candidate is more accurate
    // if accuracyDifference < SIGNIFICANT_ACCURACY_DIFFERENCE_METRES then candidate is less accurate than best

    return accuracyDifference >= SIGNIFICANT_ACCURACY_DIFFERENCE_METRES;
  }
}
