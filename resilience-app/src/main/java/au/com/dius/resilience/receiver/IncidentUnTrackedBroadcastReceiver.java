package au.com.dius.resilience.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.ui.activity.ViewIncidentActivity;

public class IncidentUnTrackedBroadcastReceiver extends BroadcastReceiver {

  private static final String TAG = IncidentUnTrackedBroadcastReceiver.class.getName();
  private ViewIncidentActivity viewIncidentActivity;

  public IncidentUnTrackedBroadcastReceiver(ViewIncidentActivity viewIncidentActivity) {
    this.viewIncidentActivity = viewIncidentActivity;
  }


  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d(TAG, "Intent received" + intent.getAction());
    if (!Intents.RESILIENCE_INCIDENT_UNTRACKED.equals(intent.getAction())) {
      return;
    }

    viewIncidentActivity.incidentUnTracked((String) intent.getExtras().get("incidentId"));

    Log.d(TAG, "Incident tracked broadcast event received!!");
  }

}
