package au.com.dius.resilience.test.unit.service;

import android.content.Intent;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.model.Feedback;
import au.com.dius.resilience.persistence.repository.Repository;
import au.com.dius.resilience.service.SendFeedbackService;
import au.com.dius.resilience.test.unit.utils.TestHelper;
import com.google.inject.AbstractModule;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static au.com.dius.resilience.intent.Intents.RESILIENCE_FEEDBACK_SUBMITTED;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class SendFeedbackServiceTest {

  private SendFeedbackService service;

  @Mock
  Repository repository;

  Intent intent;

  private final class MockRepositoryModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(Repository.class).toInstance(repository);
    }
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    TestHelper.overrideRoboguiceModule(new MockRepositoryModule());

    intent = new Intent(RESILIENCE_FEEDBACK_SUBMITTED);
    intent.putExtra(SendFeedbackService.EXTRA_FEEDBACK, new Feedback("some text", "dev-id123"));

    when(repository.sendFeedback(any(Feedback.class))).thenReturn(true);

    service = spy(new SendFeedbackService());
    service.onCreate();
  }

  @Test
  public void shouldSendCompleteBroadcastOnSave() throws NoSuchFieldException {
    when(repository.sendFeedback(any(Feedback.class))).thenReturn(true);
    service.onHandleIntent(intent);
    verify(service).sendBroadcast(new Intent(Intents.RESILIENCE_FEEDBACK_REQUESTED));
     verify(service).sendBroadcast(new Intent(Intents.RESILIENCE_FEEDBACK_SUBMITTED));
  }

  @Test
  public void shouldNotSendCompleteBroadcastOnFail() throws NoSuchFieldException {
    when(repository.sendFeedback(any(Feedback.class))).thenReturn(false);
    service.onHandleIntent(intent);
    verify(service).sendBroadcast(new Intent(Intents.RESILIENCE_FEEDBACK_REQUESTED));
    verify(service, never()).sendBroadcast(new Intent(Intents.RESILIENCE_FEEDBACK_SUBMITTED));
  }
}
