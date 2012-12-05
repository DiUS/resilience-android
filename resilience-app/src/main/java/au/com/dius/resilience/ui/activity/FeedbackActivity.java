package au.com.dius.resilience.ui.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import au.com.dius.resilience.R;
import au.com.dius.resilience.ui.fragment.FeedbackFragment;
import roboguice.activity.RoboActivity;

public class FeedbackActivity extends RoboActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feedback);
  }

  public void onFeedbackSubmitClick(View button) {
    FragmentManager fragmentManager = getFragmentManager();
    FeedbackFragment fragment = (FeedbackFragment) fragmentManager.findFragmentById(R.id.feedback_fragment);
    fragment.onFeedbackSubmitClick(button);
  }
}