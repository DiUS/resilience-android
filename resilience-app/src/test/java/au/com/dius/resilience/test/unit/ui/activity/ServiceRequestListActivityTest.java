package au.com.dius.resilience.test.unit.ui.activity;

import android.content.Intent;
import android.widget.ListView;
import au.com.dius.resilience.intent.Extras;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import au.com.dius.resilience.ui.activity.ServiceRequestListActivity;
import au.com.dius.resilience.ui.activity.ViewServiceRequestActivity;
import au.com.dius.resilience.ui.adapter.ListViewAdapter;
import au.com.justinb.open311.model.ServiceRequest;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import junitx.util.PrivateAccessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(ResilienceTestRunner.class)
public class ServiceRequestListActivityTest {

  public static final int INDEX = 50;

  private ServiceRequestListActivity serviceRequestListActivity;

  @Mock
  private ListView listView;

  @Mock
  private ListViewAdapter listViewAdapter;

  @Mock
  private ServiceRequest serviceRequest;

  @Before
  public void setup() throws NoSuchFieldException {
    serviceRequestListActivity = new ServiceRequestListActivity();
    PrivateAccessor.setField(serviceRequestListActivity, "adapter", listViewAdapter);

    when(listViewAdapter.getItem(INDEX)).thenReturn(serviceRequest);
  }

  @Test
  public void shouldStartViewServiceRequestActivityOnItemClick() {
    serviceRequestListActivity.onListItemClick(null, null, 0, 0);

    ShadowActivity shadowActivity = shadowOf(serviceRequestListActivity);
    Intent intent = shadowActivity.getNextStartedActivity();

    assertNotNull(intent);
    assertThat(intent.getComponent().getClassName(), is(ViewServiceRequestActivity.class.getName()));
  }

  @Test
  public void selectedServiceRequestIsSentWithIntent() {
    serviceRequestListActivity.onListItemClick(null, null, INDEX, 0);

    ShadowActivity shadowActivity = shadowOf(serviceRequestListActivity);
    Intent intent = shadowActivity.getNextStartedActivity();

    assertNotNull(intent);

    ServiceRequest retrievedServiceRequest = (ServiceRequest) intent.getExtras().get(Extras.SERVICE_REQUEST);
    assertSame(retrievedServiceRequest, serviceRequest);
  }

  @Test
  public void shouldSetAdapterDataWithMaximumSizeList() {
    ArrayList<ServiceRequest> incidentList = new ArrayList<ServiceRequest>();

    for (int i = 0; i < ServiceRequestListActivity.MAX_RESULT_SIZE; ++i) {
      incidentList.add(serviceRequest);
    }

    serviceRequestListActivity.onLoadFinished(null, incidentList);
    verify(listViewAdapter).setData(incidentList);
  }

  @Test
  public void shouldSetAdapterDataWithOneElement() {
    ArrayList<ServiceRequest> incidentList = new ArrayList<ServiceRequest>();
    incidentList.add(serviceRequest);

    serviceRequestListActivity.onLoadFinished(null, incidentList);
    verify(listViewAdapter).setData(incidentList);
  }

  @Test
  public void shouldNotSetAdapterDataWhenListIsEmpty() {
    ArrayList<ServiceRequest> incidentList = new ArrayList<ServiceRequest>();
    serviceRequestListActivity.onLoadFinished(null, incidentList);
    verify(listViewAdapter).setData(incidentList);
  }

//  @Test
//  public void shouldShowLoadingIconWhenLoadingIncidents() {
//
//  }

  @Test
  public void shouldShowNoIncidentsMessageWhenNoIncidentsReturned() {
    serviceRequestListActivity.onLoadFinished(null, new ArrayList<ServiceRequest>());

//    assertThat
  }
}