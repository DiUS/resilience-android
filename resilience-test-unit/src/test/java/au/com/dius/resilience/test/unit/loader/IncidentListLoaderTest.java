package au.com.dius.resilience.test.unit.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import au.com.dius.resilience.loader.IncidentListLoader;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.Repository;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import com.xtremelabs.robolectric.Robolectric;
import junitx.util.PrivateAccessor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(ResilienceTestRunner.class)
public class IncidentListLoaderTest {

  public static final String REFRESH_OBSERVER = "refreshObserver";
  public static final String DATA = "data";

  @Mock
  private Context mockContext;

  @Mock
  private BroadcastReceiver mockObserver;

  @Mock
  private Repository mockRepository;

  private IncidentListLoader listLoader;
  private ShadowLoader shadowLoader;


  @Before
  public void setUp() throws NoSuchFieldException {
    listLoader = new IncidentListLoader(mockContext, mockRepository);
    PrivateAccessor.setField(listLoader, REFRESH_OBSERVER, mockObserver);

    shadowLoader = (ShadowLoader) Robolectric.shadowOf_(listLoader);
  }

  @Test
  public void onBackgroundLoadShouldDelegateToRepository() {
    List<Incident> incidents = new ArrayList<Incident>();
    when(mockRepository.findIncidents()).thenReturn(incidents);

    assertThat(listLoader.loadInBackground(), sameInstance(incidents));
  }

  @Test
  public void shouldOnlyDeliverResultWhenInStartedState() {
    shadowLoader.setStarted(true);
    ArrayList<Incident> incidents = new ArrayList<Incident>();

    listLoader.deliverResult(incidents);
    Assert.assertSame(incidents, shadowLoader.getDeliveredResult());
  }

  @Test
  public void onStartLoadingShouldUseExistingObserver() {
    BroadcastReceiver refreshObserver = getMockObserver();

    assertNotNull(refreshObserver);

    listLoader.onStartLoading();
    assertSame(refreshObserver, getMockObserver());
  }

  @Test
  public void onStartLoadingShouldCreateNewObserverIfNotAlreadySet() throws NoSuchFieldException {
    PrivateAccessor.setField(listLoader, REFRESH_OBSERVER, null);

    assertNull(getMockObserver());

    listLoader.onStartLoading();
    assertNotNull(getMockObserver() );
  }

  @Test
  public void shouldNotDeliverResultWhenStarted() {
    shadowLoader.setStarted(false);
    ArrayList originalData = new ArrayList();
    shadowLoader.deliverResult(originalData);

    listLoader.deliverResult(new ArrayList<Incident>());

    assertSame(shadowLoader.getDeliveredResult(), originalData);
  }

  @Test
  public void onResetShouldUnregisterObserver() {
    listLoader.onReset();

    Mockito.verify(mockContext).unregisterReceiver(mockObserver);
    Object refreshObserver = getMockObserver();
    Assert.assertThat(refreshObserver, nullValue());

    Object data = getData();
    Assert.assertThat(data, nullValue());
  }

  @Test
  public void startLoadingShouldForceLoadWhenContentChanged() {
    shadowLoader.setTakeContentChanged(true);

    listLoader.onStartLoading();
    assertThat(shadowLoader.isForceLoadCalled(), is(true));
  }

  @Test
  public void startLoadingShouldForceLoadWhenDataIsNull() throws NoSuchFieldException {
    shadowLoader.setTakeContentChanged(false);
    PrivateAccessor.setField(listLoader, DATA, null);

    listLoader.onStartLoading();
    assertThat(shadowLoader.isForceLoadCalled(), is(true));
  }

  @Test
  public void startLoadingShouldDeliverResultIfDataAlreadySet() throws NoSuchFieldException {
    ArrayList<Incident> data = new ArrayList<Incident>();
    PrivateAccessor.setField(listLoader, DATA, data);
    shadowLoader.setStarted(true);

    listLoader.onStartLoading();

    assertSame(data, shadowLoader.getDeliveredResult());
    assertThat(shadowLoader.isForceLoadCalled(), is(false));
  }

  @Test
  public void startLoadingShouldNotForceLoadWhenDataIsNullOrContentHasNotChanged() throws NoSuchFieldException {
    shadowLoader.setTakeContentChanged(false);
    PrivateAccessor.setField(listLoader, DATA, new ArrayList<Incident>());

    listLoader.onStartLoading();
    assertThat(shadowLoader.isForceLoadCalled(), is(false));
  }

  private BroadcastReceiver getMockObserver() {
    try {
      return (BroadcastReceiver) PrivateAccessor.getField(listLoader, REFRESH_OBSERVER);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  private Object getData() {
    try {
      return PrivateAccessor.getField(listLoader, DATA);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

}
