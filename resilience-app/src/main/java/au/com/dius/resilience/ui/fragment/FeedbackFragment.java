package au.com.dius.resilience.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Device;
import au.com.dius.resilience.model.Feedback;
import au.com.dius.resilience.persistence.repository.Repository;
import au.com.dius.resilience.service.SendFeedbackService;
import au.com.dius.resilience.ui.activity.PreferenceActivity;

import static au.com.dius.resilience.intent.Intents.RESILIENCE_FEEDBACK_SUBMITTED;

public class FeedbackFragment extends Fragment {

  private TextView feedbackText;

  private FeedbackFragment.FeedbackSavedBroadcastReceiver feedbackSavedBroadcastReceiver;
  private Activity activity;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_feedback, container);
  }

  @Override
  public void onActivityCreated(Bundle state) {
    activity = getActivity();
    feedbackText = (TextView) activity.findViewById(R.id.feedback_text);

    activity.registerReceiver(feedbackSavedBroadcastReceiver,
      new IntentFilter(RESILIENCE_FEEDBACK_SUBMITTED));

    super.onActivityCreated(state);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    feedbackSavedBroadcastReceiver = new FeedbackSavedBroadcastReceiver();
  }

  public void onFeedbackSubmitClick(View button) {

    String deviceId = Device.getDeviceId(activity);
    String text = feedbackText.getText().toString();
    Feedback feedback = new Feedback(text, deviceId);

    if (text != null && text.trim().length() > 0) {
      button.setEnabled(false);
      activity.startService(SendFeedbackService.createFeedbackIntent(activity, feedback));
    }
  }

  @Override
  public void onPause() {
    activity.unregisterReceiver(feedbackSavedBroadcastReceiver);
    super.onPause();
  }

  @Override
  public void onResume() {
    feedbackSavedBroadcastReceiver = new FeedbackSavedBroadcastReceiver();
    activity.registerReceiver(feedbackSavedBroadcastReceiver, new IntentFilter(RESILIENCE_FEEDBACK_SUBMITTED));
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
