package au.com.dius.resilience.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Device;
import au.com.dius.resilience.model.Feedback;
import au.com.dius.resilience.persistence.repository.Repository;
import au.com.dius.resilience.service.SendFeedbackService;
import au.com.dius.resilience.ui.Themer;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import static au.com.dius.resilience.intent.Intents.RESILIENCE_FEEDBACK_SUBMITTED;

public class FeedbackActivity extends RoboActivity {

  @Inject
  private Repository repository;

  @InjectView(R.id.feedback_text)
  private TextView feedbackText;

  private FeedbackActivity.FeedbackSavedBroadcastReceiver feedbackSavedBroadcastReceiver;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Themer.applyCurrentTheme(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feedback);

    feedbackSavedBroadcastReceiver = new FeedbackSavedBroadcastReceiver();

    registerReceiver(feedbackSavedBroadcastReceiver,
      new IntentFilter(RESILIENCE_FEEDBACK_SUBMITTED));
  }

  public void onFeedbackSubmitClick(View button) {
    button.setEnabled(false);
    String deviceId = Device.getDeviceId(this);
    String text = feedbackText.getText().toString();

    Feedback feedback = new Feedback(text, deviceId);

    if (text != null && text.trim().length() > 0) {
      startService(SendFeedbackService.createFeedbackIntent(this, feedback));
    }
  }

  @Override
  protected void onPause() {
    unregisterReceiver(feedbackSavedBroadcastReceiver);
    super.onPause();
  }

  @Override
  protected void onResume() {
    feedbackSavedBroadcastReceiver = new FeedbackSavedBroadcastReceiver();
    registerReceiver(feedbackSavedBroadcastReceiver, new IntentFilter(RESILIENCE_FEEDBACK_SUBMITTED));
    super.onResume();
  }

  private final class FeedbackSavedBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      Toast toast = Toast.makeText(context.getApplicationContext(), R.string.feedback_thanks, Toast.LENGTH_LONG);
      toast.show();
      startActivity(new Intent(context, PreferenceActivity.class));
    }
  }
}