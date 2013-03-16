package au.com.dius.resilience.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.ui.activity.ViewIncidentActivity;

public class IncidentTrackedBroadcastReceiver extends BroadcastReceiver {

  private static final String TAG = IncidentTrackedBroadcastReceiver.class.getName();
  private ViewIncidentActivity viewIncidentActivity;

  public IncidentTrackedBroadcastReceiver(ViewIncidentActivity viewIncidentActivity) {
    this.viewIncidentActivity = viewIncidentActivity;
  }


  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d(TAG, "Intent received" + intent.getAction());
    if (!Intents.RESILIENCE_INCIDENT_TRACKED.equals(intent.getAction())) {
      return;
    }

    viewIncidentActivity.incidentTracked((String) intent.getExtras().get("incidentId"));

    Log.d(TAG, "Incident tracked broadcast event received!!");
  }

}
