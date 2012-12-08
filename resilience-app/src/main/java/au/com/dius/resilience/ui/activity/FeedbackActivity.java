package au.com.dius.resilience.ui.activity;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import au.com.dius.resilience.R;
import au.com.dius.resilience.persistence.repository.Repository;
import au.com.dius.resilience.ui.Themer;
import au.com.dius.resilience.ui.fragment.FeedbackFragment;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;

import static au.com.dius.resilience.intent.Intents.RESILIENCE_FEEDBACK_SUBMITTED;

public class FeedbackActivity extends RoboActivity {

  @Inject
  private Repository repository;

  private FeedbackBroadcastReceiver feedbackBroadcastReceiver;

  @Inject
  private Themer themer;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feedback);
  }

  @Override
  public void onResume() {
    feedbackBroadcastReceiver = new FeedbackBroadcastReceiver();
    registerReceiver(feedbackBroadcastReceiver, new IntentFilter(RESILIENCE_FEEDBACK_SUBMITTED));
    super.onResume();
  }

  @Override
  public void onPause() {
    unregisterReceiver(feedbackBroadcastReceiver);
    super.onPause();
  }

  public void onFeedbackSubmitClick(View button) {
    FragmentManager fragmentManager = getFragmentManager();
    FeedbackFragment fragment = (FeedbackFragment) fragmentManager.findFragmentById(R.id.feedback_fragment);
    fragment.onFeedbackSubmitClick(button);
  }

  private class FeedbackBroadcastReceiver
    extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      Toast toast = Toast.makeText(getApplicationContext(), R.string.feedback_thanks, Toast.LENGTH_LONG);
      toast.show();
      finish();
    }
  }
}