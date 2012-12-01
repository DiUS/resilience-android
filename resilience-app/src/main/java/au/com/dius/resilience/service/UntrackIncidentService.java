package au.com.dius.resilience.service;

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.Repository;
import com.google.inject.Inject;
import roboguice.service.RoboIntentService;

public class UntrackIncidentService extends RoboIntentService{
  @Inject
  private Repository repository;

  public static final String INCIDENT = UntrackIncidentService.class.getName() + ".IncidentToUnTrack";

  private static final String TAG = UntrackIncidentService.class.getName();

  public UntrackIncidentService() {
    super("ResilienceUntrackIncidentService");
  }

  public static Intent createUnTrackingIntent(Context context, Incident incident) {
    Intent trackIncident = new Intent(context, UntrackIncidentService.class);
    trackIncident.putExtra(INCIDENT, incident);

    return trackIncident;
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Incident incident = (Incident) intent.getSerializableExtra(INCIDENT);
    if (repository.untrackIncident(incident, getCurrentUserId())) {
      final Intent resultIntent = new Intent(Intents.RESILIENCE_INCIDENT_UNTRACKED);
      resultIntent.putExtra("incidentId", incident.getId());
      sendBroadcast(resultIntent);

      Log.d(TAG, "Sent broadcast");
    }
  }

  private String getCurrentUserId() {
    return ((TelephonyManager)this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
  }
}
