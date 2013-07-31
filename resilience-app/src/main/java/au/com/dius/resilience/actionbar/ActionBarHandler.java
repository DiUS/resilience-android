package au.com.dius.resilience.actionbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import au.com.dius.resilience.R;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;
import au.com.dius.resilience.ui.Codes;
import au.com.dius.resilience.ui.ResilienceActionBarThemer;
import au.com.dius.resilience.ui.activity.CreateServiceRequestActivity;
import au.com.dius.resilience.ui.activity.FeedbackActivity;
import au.com.dius.resilience.ui.activity.PreferenceActivity;
import com.google.inject.Inject;

public class ActionBarHandler {

  private Activity activity;

  @Inject
  private ResilienceActionBarThemer actionBar;

  @Inject
  private PreferenceAdapter preferenceAdapter;

  @Inject
  public ActionBarHandler(Context context) {
    this.activity = (Activity) context;
  }

  public boolean handleMenuItemSelected(MenuItem item) {
    switch (item.getItemId()) {

      case R.id.refresh:
        Intent refreshIntent = new Intent(Intents.RESILIENCE_INCIDENT_CREATED);
        activity.sendBroadcast(refreshIntent);
        break;

      case R.id.send_feedback:
        Intent feedbackIntent = new Intent(activity, FeedbackActivity.class);
        activity.startActivity(feedbackIntent);
        break;

      case R.id.user_preferences:
        Intent userPreferencesIntent = new Intent(activity, PreferenceActivity.class);
        activity.startActivity(userPreferencesIntent);
        break;

      case R.id.create_incident:
        Intent raiseIncidentIntent = new Intent(activity, CreateServiceRequestActivity.class);
        activity.startActivityForResult(raiseIncidentIntent, Codes.CreateIncident.REQUEST_CODE);
        break;

      default:
        return false;
    }

    return true;
  }
}
