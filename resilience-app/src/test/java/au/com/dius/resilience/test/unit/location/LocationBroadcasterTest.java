package au.com.dius.resilience.test.unit.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Parcelable;
import au.com.dius.resilience.R;
import au.com.dius.resilience.intent.Extras;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.location.BestLocationDelegate;
import au.com.dius.resilience.location.LocationBroadcaster;
import au.com.dius.resilience.location.StopLocatingRunnable;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import com.xtremelabs.robolectric.Robolectric;
import junitx.util.PrivateAccessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(ResilienceTestRunner.class)
public class LocationBroadcasterTest {

  private LocationBroadcaster locationBroadcaster;

  private LocationManager locationManagerSpy;

  private Resources resources;

  @Mock
  private Location location;

  @Mock
  private ScheduledThreadPoolExecutor threadPoolExecutor;

  @Mock
  private StopLocatingRunnable stopLocatingRunnable;

  @Mock
  private BestLocationDelegate bestLocationDelegate;

  @Before
  public void setup() throws Exception {
    locationBroadcaster = new LocationBroadcaster();

    resources = Robolectric.application.getApplicationContext().getResources();
    LocationManager locationManager = Robolectric.newInstanceOf(LocationManager.class);
    locationManagerSpy = spy(locationManager);
    PrivateAccessor.setField(locationBroadcaster, "locationManager", locationManagerSpy);
    PrivateAccessor.setField(locationBroadcaster, "context", Robolectric.application.getApplicationContext());
    PrivateAccessor.setField(locationBroadcaster, "threadPoolExecutor", threadPoolExecutor);
    PrivateAccessor.setField(locationBroadcaster, "bestLocationDelegate", bestLocationDelegate);
  }

  @Test
  public void shouldRequestLocationUpdatesFromAllAvailableSources() {
    locationBroadcaster.startPolling();

    verify(locationManagerSpy).requestLocationUpdates(LocationManager.GPS_PROVIDER,
      resources.getInteger(R.integer.minimum_location_age_ms),
      resources.getInteger(R.integer.minimum_location_accuracy_meters), locationBroadcaster);

    verify(locationManagerSpy).requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
      resources.getInteger(R.integer.minimum_location_age_ms),
      resources.getInteger(R.integer.minimum_location_accuracy_meters), locationBroadcaster);

    verify(locationManagerSpy).requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
      resources.getInteger(R.integer.minimum_location_age_ms),
      resources.getInteger(R.integer.minimum_location_accuracy_meters), locationBroadcaster);
  }

  @Test
  public void shouldBroadcastLocationOnLocationUpdate() {

    TestBroadcastLocationReceiver locationUpdatedReceiver
      = new TestBroadcastLocationReceiver(Intents.RESILIENCE_LOCATION_UPDATED, location);

    Robolectric.application.registerReceiver(locationUpdatedReceiver,
      new IntentFilter(Intents.RESILIENCE_LOCATION_UPDATED));

    locationBroadcaster.onLocationChanged(location);
  }

  @Test
  public void shouldStopPollingOnLocationChange() {
    locationBroadcaster.onLocationChanged(location);
    verify(locationManagerSpy).removeUpdates(locationBroadcaster);
  }

  @Test
  public void shouldScheduleTimeoutOnLocationPoll() {
    locationBroadcaster.startPolling();
    verify(threadPoolExecutor).schedule(any(StopLocatingRunnable.class), eq(LocationBroadcaster.TIMEOUT), eq(TimeUnit.SECONDS));
  }

  @Test
  public void shouldRemoveLocationUpdates() {
    locationBroadcaster.stopPolling();
    verify(locationManagerSpy).removeUpdates(locationBroadcaster);
  }

  @Test
  public void shouldDelegateFindingBestLocation() {
    locationBroadcaster.startPolling();
    verify(bestLocationDelegate).getBestLastKnownLocation();
  }

  private class TestBroadcastLocationReceiver extends BroadcastReceiver {

    private final String action;
    private final Location goodCachedLocation;

    public TestBroadcastLocationReceiver(String anAction, Location aGoodCachedLocation) {
      action = anAction;
      goodCachedLocation = aGoodCachedLocation;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
      assertThat(intent.getAction(), is(action));
      Parcelable parcelableExtra = intent.getParcelableExtra(Extras.LOCATION);
      assertNotNull(parcelableExtra);
      assertSame(parcelableExtra, goodCachedLocation);
    }
  }
}
