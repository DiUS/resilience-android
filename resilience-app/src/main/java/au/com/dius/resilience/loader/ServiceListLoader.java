package au.com.dius.resilience.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.observer.IntentBasedLoaderNotifierBroadcastReceiver;
import au.com.justinb.open311.GenericRequestAdapter;
import au.com.justinb.open311.model.ServiceList;

import java.util.List;

public class ServiceListLoader extends AbstractAsyncListLoader<ServiceList> {

  private GenericRequestAdapter<ServiceList> serviceListRequest
    = new GenericRequestAdapter<ServiceList>(ServiceList.class);

  public ServiceListLoader(Context context) {
    super(context);
  }

  @Override
  public BroadcastReceiver createBroadcastReceiver() {
    return new IntentBasedLoaderNotifierBroadcastReceiver(this, new IntentFilter(Intents.RESILIENCE_SERVICE_LIST_LOADED));
  }

  @Override
  public List<ServiceList> loadInBackground() {
    return serviceListRequest.list();
  }
}
