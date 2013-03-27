package au.com.dius.resilience.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.observer.IntentBasedLoaderNotifierBroadcastReceiver;
import au.com.justinb.open311.GenericRequestAdapter;
import au.com.justinb.open311.model.ServiceRequest;

import java.util.List;

public class ServiceRequestLoader extends AbstractAsyncListLoader<ServiceRequest> {

  public static final int SERVICE_REQUEST_LIST_LOADER = 0;

  private GenericRequestAdapter<ServiceRequest> requestAdapter
    = new GenericRequestAdapter<ServiceRequest>(ServiceRequest.class);

  public ServiceRequestLoader(Context context) {
    super(context);
  }

  @Override
  public BroadcastReceiver createBroadcastReceiver() {
    return new IntentBasedLoaderNotifierBroadcastReceiver(this, new IntentFilter(Intents.RESILIENCE_INCIDENT_CREATED));
  }

  @Override
  public List<ServiceRequest> loadInBackground() {
    return requestAdapter.list();
  }
}
