package au.com.dius.resilience.location;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import au.com.dius.resilience.R;
import au.com.dius.resilience.intent.Extras;
import au.com.dius.resilience.intent.Intents;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ContextSingleton
public class LocationBroadcaster implements LocationListener {

  public static final long TIMEOUT = 20;

  @Inject
  private LocationManager locationManager;

  @Inject
  private Context context;

  private final ScheduledExecutorService threadPoolExecutor;

  @Inject
  public LocationBroadcaster() {
    threadPoolExecutor = Executors.newSingleThreadScheduledExecutor();
  }

  public void startPolling() {

    // TODO - Use last known location if it's good enough.

    threadPoolExecutor.schedule(new StopLocatingRunnable(), TIMEOUT, TimeUnit.SECONDS);

    for (String provider : locationManager.getAllProviders()) {
      locationManager.requestLocationUpdates(provider,
        context.getResources().getInteger(R.integer.minimum_location_age_ms),
        context.getResources().getInteger(R.integer.minimum_location_accuracy_meters), this);
    }
  }

  @Override
  public void onLocationChanged(Location location) {
    Intent intent = new Intent(Intents.RESILIENCE_LOCATION_UPDATED);
    Bundle extras = new Bundle();
    extras.putParcelable(Extras.LOCATION, location);
    intent.putExtras(extras);
    context.sendBroadcast(intent);

    stopPolling();
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
  }

  @Override
  public void onProviderEnabled(String provider) {
  }

  @Override
  public void onProviderDisabled(String provider) {
  }

  public void stopPolling() {
    locationManager.removeUpdates(this);
  }
}
