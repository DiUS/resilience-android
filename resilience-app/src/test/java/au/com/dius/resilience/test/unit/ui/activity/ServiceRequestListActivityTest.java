package au.com.dius.resilience.test.unit.ui.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.view.View;
import android.widget.AbsListView;
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

import static au.com.dius.resilience.loader.ServiceRequestLoader.SERVICE_REQUEST_LIST_LOADER;
import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static junitx.util.PrivateAccessor.setField;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(ResilienceTestRunner.class)
public class ServiceRequestListActivityTest {

  public static final int INDEX = 50;
  public static final int LAST_VISIBLE_POSITION = 1;

  private ServiceRequestListActivity serviceRequestListActivity;

  @Mock
  private ListViewAdapter listViewAdapter;

  @Mock
  private ServiceRequest serviceRequest;

  @Mock
  private View button;

  @Mock
  private Loader<Object> serviceRequestLoader;

  private LoaderManager loaderManagerMock;

  private ListView listViewMock;

  @Before
  public void setup() throws NoSuchFieldException {
    serviceRequestListActivity = new ServiceRequestListActivity();

    when(listViewAdapter.getCount()).thenReturn(LAST_VISIBLE_POSITION + 1);
    when(listViewAdapter.getItem(INDEX)).thenReturn(serviceRequest);
    setField(serviceRequestListActivity, "adapter", listViewAdapter);

    loaderManagerMock = serviceRequestListActivity.getLoaderManager();
    when(loaderManagerMock.getLoader(SERVICE_REQUEST_LIST_LOADER)).thenReturn(serviceRequestLoader);

    listViewMock = serviceRequestListActivity.getListView();
    when(listViewMock.getLastVisiblePosition()).thenReturn(LAST_VISIBLE_POSITION);
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
  public void shouldSetAdapterDataWithOneElement() {
    ArrayList<ServiceRequest> incidentList = new ArrayList<ServiceRequest>();
    incidentList.add(serviceRequest);

    serviceRequestListActivity.onLoadFinished(null, incidentList);
    verify(listViewAdapter).addAll(incidentList);
  }

  @Test
  public void shouldNotSetAdapterDataWhenListIsEmpty() {
    ArrayList<ServiceRequest> incidentList = new ArrayList<ServiceRequest>();
    serviceRequestListActivity.onLoadFinished(null, incidentList);
    verify(listViewAdapter).addAll(incidentList);
  }

  @Test
  public void shouldFlagContentChangedOnLoaderWhenScrolledToEnd() {
    serviceRequestListActivity.onScrollStateChanged(listViewMock, AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
    verify(serviceRequestLoader).onContentChanged();
  }

  @Test
  public void shouldNotFlagContentChangedOnLoaderWhenNotAtEnd() {
    given(listViewMock.getLastVisiblePosition()).willReturn(0);
    serviceRequestListActivity.onScrollStateChanged(listViewMock, AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
    verify(serviceRequestLoader, never()).onContentChanged();
  }

  @Test
  public void shouldNotFlagContentChangedWhenLoaderIsNull() {
    given(loaderManagerMock.getLoader(SERVICE_REQUEST_LIST_LOADER)).willReturn(null);
    serviceRequestListActivity.onScrollStateChanged(listViewMock, AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
    verifyZeroInteractions(listViewMock);
    verifyZeroInteractions(serviceRequestLoader);
  }

  @Test
  public void shouldDoNothingForNonIdleScrollStates() {
    serviceRequestListActivity.onScrollStateChanged(listViewMock, AbsListView.OnScrollListener.SCROLL_STATE_FLING);
    verifyZeroInteractions(listViewMock);
    verifyZeroInteractions(serviceRequestLoader);
  }

  // TODO
//  @Test
//  public void shouldShowLoadingIconWhenLoadingIncidents() {
//
//  }

  // TODO
//  @Test
//  public void shouldShowNoIncidentsMessageWhenNoIncidentsReturned() {
//    serviceRequestListActivity.onLoadFinished(null, new ArrayList<ServiceRequest>());
//
////    assertThat
//  }
}