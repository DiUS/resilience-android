package au.com.dius.resilience.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import au.com.dius.resilience.R;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.observer.IntentBasedLoaderNotifierBroadcastReceiver;
import au.com.dius.resilience.util.Logger;
import au.com.justinb.open311.GenericRequestAdapter;
import au.com.justinb.open311.model.ServiceRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceRequestLoader extends AbstractAsyncListLoader<ServiceRequest> {

  public static final int SERVICE_REQUEST_LIST_LOADER = 0;

  public static final String PAGE_PROPERTY = "page";

  private GenericRequestAdapter<ServiceRequest> requestAdapter;

  private Map<String, String> extraProperties = new HashMap<String, String>();

  private int page = 1;

  public ServiceRequestLoader(Context context) {
    super(context);
    requestAdapter = new GenericRequestAdapter<ServiceRequest>(ServiceRequest.class);
  }

  @Override
  public BroadcastReceiver createBroadcastReceiver() {
    return new IntentBasedLoaderNotifierBroadcastReceiver(this, new IntentFilter(Intents.RESILIENCE_INCIDENT_CREATED));
  }

  @Override
  public List<ServiceRequest> loadInBackground() {
    extraProperties.put(PAGE_PROPERTY, String.valueOf(page));

    Logger.d(this, "Loading page", page, "of service requests from", getContext().getString(R.string.open_311_base_url));

    List<ServiceRequest> list = requestAdapter.list(extraProperties);

    // TODO - perhaps this should only increment when size = max page size?
    // TOOD - otherwise could lose issues if a full page wasn't loaded on the last refresh.
    if (list.size() > 0) {
      ++page;
    }

    Logger.d(this, "Loaded ", list.size(), " service requests.");

    return list;
  }
}
