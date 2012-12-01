package au.com.dius.resilience.service;

import android.content.Intent;
import android.util.Log;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.Repository;
import com.google.inject.Inject;
import roboguice.service.RoboIntentService;

public class CreateIncidentService extends RoboIntentService {

  public static final String EXTRA_INCIDENT = CreateIncidentService.class.getName() + ".IncidentToCreate";

  private static final String LOG_TAG = CreateIncidentService.class.getName();

  @Inject
  private Repository repository;

  public CreateIncidentService() {
    super("ResilienceSaveIncidentService");
  }

  @Override
  public void onHandleIntent(Intent intent) {

    Log.d(LOG_TAG, "Intent is being handled" + intent);
    Log.d(LOG_TAG, "repository is " + repository);

    Incident incident = (Incident) intent.getExtras().getSerializable(CreateIncidentService.EXTRA_INCIDENT);
    Log.d(LOG_TAG, "Incident is " + incident);

    if (repository.createIncident(incident)) {
      sendBroadcast(new Intent(Intents.RESILIENCE_INCIDENT_ADDED));
    }
  }

}
