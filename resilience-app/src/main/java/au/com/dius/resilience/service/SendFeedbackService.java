package au.com.dius.resilience.service;

import android.content.Intent;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.model.Feedback;
import au.com.dius.resilience.persistence.repository.Repository;
import com.google.inject.Inject;
import roboguice.service.RoboIntentService;

import static au.com.dius.resilience.intent.Intents.RESILIENCE_FEEDBACK_SUBMITTED;

public class SendFeedbackService extends RoboIntentService {

  @Inject
  private Repository repository;

  public static final String EXTRA_FEEDBACK = SendFeedbackService.class.getName() + ".Feedback";

  public SendFeedbackService() {
    super("ResilienceSendFeedbackIncidentService");
  }

  @Override
  public void onHandleIntent(Intent intent) {
    Feedback feedback = (Feedback) intent.getExtras().getSerializable(EXTRA_FEEDBACK);

    if(repository.sendFeedback(feedback)) {
      sendBroadcast(new Intent(RESILIENCE_FEEDBACK_SUBMITTED));
    }
  }
}
