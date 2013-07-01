package au.com.dius.resilience.location;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import au.com.dius.resilience.R;
import au.com.dius.resilience.event.Publisher;
import au.com.dius.resilience.intent.Extras;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.location.event.LocationUpdatedEvent;import au.com.dius.resilience.util.Logger;
import com.google.inject.Inject;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import roboguice.inject.ContextSingleton;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ContextSingleton
public class LocationBroadcaster implements LocationListener, Publisher {

  public static final long TIMEOUT = 60;

  @Inject
  private LocationManager locationManager;

  @Inject
  private Context context;

  private final ScheduledExecutorService threadPoolExecutor;

  @Inject
  private BestLocationDelegate bestLocationDelegate;

  private final Bus bus;

  @Inject
  public LocationBroadcaster() {
    threadPoolExecutor = Executors.newSingleThreadScheduledExecutor();
    bus = new Bus();
  }

  public void startPolling() {

    Location lastLocation = bestLocationDelegate.getBestLastKnownLocation();

    if (lastLocation == null) {
      requestLocationUpdates();
    } else {
      bus.post(new LocationUpdatedEvent(lastLocation));
    }
  }

  private void requestLocationUpdates() {
    for (String provider : locationManager.getAllProviders()) {
      locationManager.requestLocationUpdates(provider,
        context.getResources().getInteger(R.integer.minimum_location_age_ms),
        context.getResources().getInteger(R.integer.minimum_location_accuracy_meters), this);
    }
    threadPoolExecutor.schedule(new StopLocatingRunnable(this), TIMEOUT, TimeUnit.SECONDS);
  }

  @Override
  public void onLocationChanged(Location location) {
    bus.post(new LocationUpdatedEvent(location));
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
    try {
      threadPoolExecutor.awaitTermination(0, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Logger.e(this, "Interrupted while terminating location timeout thread.");
    } finally {
      locationManager.removeUpdates(this);
    }
  }

  @Override
  public void subscribe(Object subscriber) {
    bus.register(subscriber);
  }

  @Override
  public void unsubscribe(Object unsubscriber) {
    bus.unregister(unsubscriber);
  }

  @Produce
  public LocationUpdatedEvent produceLocation() {
    return new LocationUpdatedEvent(bestLocationDelegate.getBestLastKnownLocation());
  }
}
