package au.com.dius.resilience.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import au.com.dius.resilience.R;
import au.com.dius.resilience.event.Publisher;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.loader.event.ServiceRequestLoadFailed;
import au.com.dius.resilience.location.event.LocationUpdatedEvent;
import au.com.dius.resilience.observer.IntentBasedLoaderNotifierBroadcastReceiver;
import au.com.dius.resilience.util.Logger;
import au.com.justinb.open311.GenericRequestAdapter;
import au.com.justinb.open311.Open311Exception;
import au.com.justinb.open311.model.ServiceRequest;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceRequestLoader extends AbstractAsyncListLoader<ServiceRequest>
  implements Publisher {

  public static final int SERVICE_REQUEST_LIST_LOADER = 0;

  private static final String PAGE_PROPERTY = "page";
  private static final String LAT = "lat";
  private static final String LONG = "long";
  private static final String RADIUS = "radius";
  private static final String DEFAULT_RADIUS = "500";

  private GenericRequestAdapter<ServiceRequest> requestAdapter;

  private Map<String, String> extraProperties = new HashMap<String, String>();

  private int page = 1;

  private final Bus bus;
  private Location lastKnownLocation;

  public ServiceRequestLoader(Context context) {
    super(context);
    requestAdapter = new GenericRequestAdapter<ServiceRequest>(ServiceRequest.class);
    bus = new Bus();
  }

  @Override
  public BroadcastReceiver createBroadcastReceiver() {
    return new IntentBasedLoaderNotifierBroadcastReceiver(this, new IntentFilter(Intents.RESILIENCE_INCIDENT_CREATED));
  }

  @Override
  public List<ServiceRequest> loadInBackground() {

    if (lastKnownLocation == null) {
      Logger.d(this, "Haven't located you yet!");
      return new ArrayList<ServiceRequest>();
    }

    extraProperties.put(PAGE_PROPERTY, String.valueOf(page));
    extraProperties.put(LAT, String.valueOf(lastKnownLocation.getLatitude()));
    extraProperties.put(LONG, String.valueOf(lastKnownLocation.getLongitude()));
    extraProperties.put(RADIUS, DEFAULT_RADIUS);

    Logger.d(this, "Loading page", page, "of service requests from", getContext().getString(R.string.open_311_base_url));
    Logger.d(this, "Querying area around ", String.valueOf(lastKnownLocation.getLatitude()), String.valueOf(lastKnownLocation.getLongitude()));

    List<ServiceRequest> list = new ArrayList<ServiceRequest>();
    try {
      list.addAll(requestAdapter.list(extraProperties));

      // TODO - perhaps this should only increment when size = max page size?
      // TOOD - otherwise could lose issues if a full page wasn't loaded on the last refresh.
      if (list.size() > 0) {
        ++page;
      }

      Logger.d(this, "Loaded ", list.size(), " service requests.");

    } catch (Open311Exception e) {
      new Handler(Looper.getMainLooper()).post(new Runnable() {
        @Override
        public void run() {
          bus.post(new ServiceRequestLoadFailed());
        }
      });
      Logger.e(this, "Error retrieving service requests: " + e.getCause().getMessage());
    }


    return list;
  }

  @Subscribe
  public void onLocationUpdatedEvent(LocationUpdatedEvent event) {
    lastKnownLocation = event.getLocation();
  }

  @Override
  public void subscribe(Object subscriber) {
    bus.register(subscriber);
  }

  @Override
  public void unsubscribe(Object unsubscriber) {
    bus.unregister(unsubscriber);
  }
}
