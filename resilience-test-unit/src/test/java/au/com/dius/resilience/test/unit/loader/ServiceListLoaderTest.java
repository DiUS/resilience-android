package au.com.dius.resilience.test.unit.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import au.com.dius.resilience.loader.ServiceListLoader;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import au.com.justinb.open311.GenericRequestAdapter;
import au.com.justinb.open311.model.ServiceList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static junitx.util.PrivateAccessor.setField;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(ResilienceTestRunner.class)
public class ServiceListLoaderTest {

  private ServiceListLoader serviceListLoader;

  @Mock
  private ServiceList serviceList1;

  @Mock
  private ServiceList serviceList2;

  @Mock
  private GenericRequestAdapter<ServiceList> genericRequestAdapter;

  @Mock
  private Context context;

  @Before
  public void setup() throws NoSuchFieldException {
    serviceListLoader = new ServiceListLoader(context);

    ArrayList<ServiceList> serviceLists = new ArrayList<ServiceList>();
    serviceLists.add(serviceList1);
    serviceLists.add(serviceList2);
    when(genericRequestAdapter.list()).thenReturn(serviceLists);

    setField(serviceListLoader, "serviceListRequest", genericRequestAdapter);
  }

  @Test
  public void shouldReturnListOfServices() {
    List<ServiceList> serviceLists = serviceListLoader.loadInBackground();

    verify(genericRequestAdapter).list();
    assertThat(serviceLists.size(), is(2));
    assertThat(serviceLists.get(0), is(serviceList1));
    assertThat(serviceLists.get(1), is(serviceList2));
  }

  @Test
  public void shouldCreateBroadcastReceiver() throws NoSuchFieldException {
    BroadcastReceiver broadcastReceiver = serviceListLoader.createBroadcastReceiver();
    // TODO - do this properly.
    assertNotNull(broadcastReceiver);
  }
}
