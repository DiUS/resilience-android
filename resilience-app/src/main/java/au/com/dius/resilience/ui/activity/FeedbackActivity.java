package au.com.dius.resilience.ui.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import au.com.dius.resilience.R;
import au.com.dius.resilience.persistence.repository.Repository;
import au.com.dius.resilience.ui.Themer;
import au.com.dius.resilience.ui.fragment.FeedbackFragment;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;

public class FeedbackActivity extends RoboActivity {

  @Inject
  private Repository repository;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Themer.applyCurrentTheme(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feedback);
  }

  public void onFeedbackSubmitClick(View button) {
    FragmentManager fragmentManager = getFragmentManager();
    FeedbackFragment fragment = (FeedbackFragment) fragmentManager.findFragmentById(R.id.feedback_fragment);
    fragment.onFeedbackSubmitClick(button);
  }
}