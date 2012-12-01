package au.com.dius.resilience.test.unit.service;

import android.content.Intent;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.Repository;
import au.com.dius.resilience.service.CreateIncidentService;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import com.xtremelabs.robolectric.Robolectric;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import roboguice.RoboGuice;

import static au.com.dius.resilience.test.shared.utils.TestHelper.assertContainsIntents;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(ResilienceTestRunner.class)
public class SaveIncidentServiceTest {

  private CreateIncidentService service;
  private Intent intent;
  private Incident incident;
  private Repository repository;

  @Before
  public void setup() {
    service = new CreateIncidentService();
    service.onCreate();

    repository = spy(RoboGuice.getBaseApplicationInjector(Robolectric.application).getInstance(Repository.class));

    intent = new Intent("");

    incident = new Incident();
    intent.getExtras().putSerializable(CreateIncidentService.EXTRA_INCIDENT, incident);
  }

  @Test
  public void shouldDelegateToRepositoryForSave() {
    service.onHandleIntent(intent);
    verify(repository).findIncidents();
  }

  @Test
  public void shouldRaiseIncidentSavedIntent() {
    service.onHandleIntent(intent);
    assertContainsIntents(Robolectric.shadowOf(service).getBroadcastIntents(), Intents.RESILIENCE_INCIDENT_CREATED);
  }
}
