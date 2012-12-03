package au.com.dius.resilience.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Feedback;
import au.com.dius.resilience.persistence.repository.Repository;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class FeedbackActivity extends RoboActivity {

  @Inject
  private Repository repository;

  @InjectView(R.id.feedback_text)
  private TextView feedbackText;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feedback);
  }

  public void onFeedbackSubmitClick(View button) {
    // TODO - put in common place.
    final String deviceId = ((TelephonyManager) this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
    final CharSequence text = feedbackText.getText();

    // TODO - bad. (replace with service call)
    if (text != null) {
      AsyncTask.execute(new Runnable() {
        @Override
        public void run() {
          repository.sendFeedback(new Feedback(text.toString(), deviceId));
        }
      });
    }
  }
}