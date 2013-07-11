package au.com.dius.resilience.test.unit.observer;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import au.com.dius.resilience.loader.ServiceRequestLoader;
import au.com.dius.resilience.observer.IntentBasedLoaderNotifierBroadcastReceiver;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class IntentBasedLoaderNotifierBroadcastReceiverTest {

  @Mock
  private Context mockContext;

  @Mock
  private ServiceRequestLoader mockLoader;

  private IntentBasedLoaderNotifierBroadcastReceiver receiver;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    when(mockLoader.getContext()).thenReturn(mockContext);
  }

  @Test
  public void shouldRegisterSingleIntentFilter() {
    IntentFilter filter = new IntentFilter("");

    receiver = new IntentBasedLoaderNotifierBroadcastReceiver(mockLoader, filter);
    verify(mockContext).registerReceiver(receiver, filter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionIfFilterIsNotSpecified() {
    receiver = new IntentBasedLoaderNotifierBroadcastReceiver(mockLoader, null);
  }

  @Test
  public void shouldRegisterMultipleReceivers() {
    IntentFilter filter1 = new IntentFilter("Filter1");
    IntentFilter filter2 = new IntentFilter("Filter2");
    IntentFilter filter3 = new IntentFilter("Filter3");

    receiver = new IntentBasedLoaderNotifierBroadcastReceiver(mockLoader, filter1, filter2, filter3);
    verify(mockContext).registerReceiver(receiver, filter1);
    verify(mockContext).registerReceiver(receiver, filter2);
    verify(mockContext).registerReceiver(receiver, filter3);
  }

  @Test
  public void shouldNotifyLoaderOfBroadcastReceipt() {
    final IntentFilter intentFilter = new IntentFilter("");
    receiver = new IntentBasedLoaderNotifierBroadcastReceiver(mockLoader, intentFilter);

    receiver.onReceive(mockContext, new Intent());
    verify(mockLoader).onContentChanged();
  }

}
