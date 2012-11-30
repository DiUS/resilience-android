package au.com.dius.resilience.service;

import android.content.Intent;
import android.util.Log;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.Repository;
import com.google.inject.Inject;
import roboguice.service.RoboIntentService;

public class SaveIncidentService extends RoboIntentService {

  public static final String EXTRA_INCIDENT = SaveIncidentService.class.getName() + ".IncidentToSave";

  private static final String LOG_TAG = SaveIncidentService.class.getName();

  @Inject
  private Repository repository;

  public SaveIncidentService() {
    super("ResilienceSaveIncidentService");
  }

  @Override
  public void onHandleIntent(Intent intent) {

    repository.findIncidents();

    Log.d(LOG_TAG, "Intent is being handled" + intent);
    Log.d(LOG_TAG, "repository is " + repository);

    Incident incident = (Incident) intent.getExtras().getSerializable(SaveIncidentService.EXTRA_INCIDENT);
    Log.d(LOG_TAG, "Incident is " + incident);

    sendBroadcast(new Intent(Intents.RESILIENCE_INCIDENT_ADDED));
  }

}
