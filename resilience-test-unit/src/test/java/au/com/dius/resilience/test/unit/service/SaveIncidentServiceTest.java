package au.com.dius.resilience.test.unit.service;

import android.content.Intent;
import android.os.Bundle;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.Repository;
import au.com.dius.resilience.service.CreateIncidentService;
import au.com.dius.resilience.test.shared.utils.TestHelper;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import com.google.inject.AbstractModule;
import com.xtremelabs.robolectric.Robolectric;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static au.com.dius.resilience.test.shared.utils.TestHelper.assertContainsIntents;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(ResilienceTestRunner.class)
public class SaveIncidentServiceTest {

  private CreateIncidentService service;

  @Mock
  private Repository repository;

  @Mock
  private Incident incident;

  @Mock
  private Intent intent;

  @Mock
  private Bundle bundle;

  private final class MockRepositoryModule extends AbstractModule {

    @Override
    protected void configure() {
      bind(Repository.class).toInstance(repository);
    }
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    TestHelper.overrideRoboguiceModule(new MockRepositoryModule());

    service = new CreateIncidentService();
    service.onCreate();

    when(intent.getExtras()).thenReturn(bundle);
    when(bundle.getSerializable(CreateIncidentService.EXTRA_INCIDENT)).thenReturn(incident);
  }

  @Test
  public void shouldDelegateToRepositoryForSave() {
    service.onHandleIntent(intent);
    verify(repository).createIncident(incident);
  }

  @Test
  public void shouldRaiseIncidentSavedIntent() {
    when(repository.createIncident(incident)).thenReturn(true);
    service.onHandleIntent(intent);
    assertContainsIntents(Robolectric.shadowOf(service).getBroadcastIntents(), Intents.RESILIENCE_INCIDENT_CREATED);
  }
}
