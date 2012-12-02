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

public class TrackIncidentService extends RoboIntentService {

  @Inject
  private Repository repository;

  public static final String INCIDENT = TrackIncidentService.class.getName() + ".IncidentToTrack";

  private static final String TAG = TrackIncidentService.class.getName();

  public TrackIncidentService() {
    super("ResilienceTrackIncidentService");
  }

  public static Intent createTrackingIntent(Context context, Incident incident) {
    Intent trackIncident = new Intent(context, TrackIncidentService.class);
    trackIncident.putExtra(INCIDENT, incident);

    return trackIncident;
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Incident incident = (Incident) intent.getSerializableExtra(INCIDENT);
    if (repository.trackIncident(incident, getCurrentUserId())) {
      final Intent resultIntent = new Intent(Intents.RESILIENCE_INCIDENT_TRACKED);
      resultIntent.putExtra("incidentId", incident.getId());
      sendBroadcast(resultIntent);

      Log.d(TAG, "Sent broadcast");
    }
  }

  private String getCurrentUserId() {
    return ((TelephonyManager)this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
  }
}
