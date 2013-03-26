package au.com.dius.resilience.test.unit.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import au.com.dius.resilience.loader.ServiceRequestLoader;
import au.com.dius.resilience.observer.IntentBasedLoaderNotifierBroadcastReceiver;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import au.com.justinb.open311.GenericRequestAdapter;
import au.com.justinb.open311.model.ServiceRequest;
import com.xtremelabs.robolectric.Robolectric;
import junitx.util.PrivateAccessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@RunWith(ResilienceTestRunner.class)
public class ServiceRequestLoaderTest {

  private ServiceRequestLoader listLoader;

  @Mock
  private Context context;

  @Mock
  private GenericRequestAdapter<ServiceRequest> requestAdapter;

  private ShadowLoader shadowLoader;

  private ArrayList<ServiceRequest> serviceRequests;

  @Before
  public void setUp() throws NoSuchFieldException {
    listLoader = new ServiceRequestLoader(context);
    shadowLoader = (ShadowLoader) Robolectric.shadowOf_(listLoader);

    serviceRequests = new ArrayList<ServiceRequest>();
    serviceRequests.add(new ServiceRequest());
    serviceRequests.add(new ServiceRequest());

    when(requestAdapter.list()).thenReturn(serviceRequests);

    PrivateAccessor.setField(listLoader, "requestAdapter", requestAdapter);
  }

  @Test
  public void shouldRegisterBroadcastReceiver() {
    BroadcastReceiver broadcastReceiver = listLoader.createBroadcastReceiver();

    assertThat(broadcastReceiver.getClass().toString(),
      is("class " + IntentBasedLoaderNotifierBroadcastReceiver.class.getName()));
  }

  @Test
  public void shouldRetrieveServiceRequests() {
    List<ServiceRequest> serviceRequests = listLoader.loadInBackground();

    assertNotNull(serviceRequests);
    assertThat(serviceRequests.size(), is(2));
  }
}
