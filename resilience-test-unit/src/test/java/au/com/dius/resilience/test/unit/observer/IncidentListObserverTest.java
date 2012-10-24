package au.com.dius.resilience.test.unit.observer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import au.com.dius.resilience.loader.IncidentListLoader;
import au.com.dius.resilience.observer.IncidentListObserver;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@RunWith(RobolectricTestRunner.class)
public class IncidentListObserverTest {

  @Mock
  private IncidentListLoader listLoader;

  @Mock
  private Context context;

  private IncidentListObserver observer;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    Mockito.when(listLoader.getContext()).thenReturn(context);
    observer = new IncidentListObserver(listLoader);
  }

  @Test
  public void shouldRegisterAsReceiverWithFilter() {
    Mockito.verify(context).registerReceiver(Matchers.eq(observer), Matchers.argThat(new BaseMatcher<IntentFilter>() {
      @Override
      public boolean matches(Object o) {
        IntentFilter filter = (IntentFilter)o;
        return filter.getAction(0).equals(IncidentListLoader.INCIDENT_LIST_LOADER_FILTER);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Action doesn't match " + IncidentListLoader.INCIDENT_LIST_LOADER_FILTER);
      }
    }));
  }

  @Test
  public void shouldTriggerContentChangeOnLoader() {
    observer.onReceive(context, new Intent());
    Mockito.verify(listLoader).onContentChanged();
  }
}
