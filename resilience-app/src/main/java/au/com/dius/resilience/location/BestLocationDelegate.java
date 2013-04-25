package au.com.dius.resilience.location;

import android.location.Location;
import android.location.LocationManager;
import au.com.dius.resilience.factory.TimeFactory;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class BestLocationDelegate {

  @Inject
  private LocationManager locationManager;

  @Inject
  private TimeFactory timeFactory;

  private Location bestLastKnownLocation;

  public Location getBestLastKnownLocation() {

    Location bestLocation = null;
    for (String provider : locationManager.getAllProviders()) {
      Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
      bestLocationOf(lastKnownLocation, bestLocation);
    }

    return bestLocation;
  }

  // TODO - Just hacking here, test me please.
  public Location bestLocationOf(Location candidateLocation, Location bestLocation) {

    if (candidateLocation == null) {
      return bestLocation;
    }

    // If last location is too old to even bother comparing with bestLocation,
    // assume bestLocation is still superior.
//    long candidateLocationAge = timeFactory.currentTimeMillis() - candidateLocation.getTime();
//    if (candidateLocationAge > MIN_AGE) {
//      return bestLocation;
//    }

//    if (candidateLocation.getAccuracy() > MIN_ACCURACY_METRES) {
//      return bestLocation;
//    }

    // The last location is within the threshold of acceptability and we don't have a
    // good location yet. So candidateLocation must be the new best.
    if (bestLocation == null) {
      return candidateLocation;
    }

    // candidateLocation ought to only be thrown out
    // if it's significantly older.
//    if (isSignificantlyOlderThan(candidateLocation, bestLocation)) {
//      return bestLocation;
//    }

    // candidateLocation is new enough - if it's more accurate than the best,
    // then use it.
    if (candidateLocation.getAccuracy() < bestLocation.getAccuracy()) {
      return candidateLocation;
    }
    else {
      return bestLocation;
    }
  }
}
