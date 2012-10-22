package au.com.dius.resilience.test.unit.persistence.repository.impl;

import au.com.dius.resilience.model.Impact;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.PhotoRepository;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;
import au.com.dius.resilience.persistence.repository.impl.ParseIncidentAdapter;
import au.com.dius.resilience.persistence.repository.impl.ParseIncidentRepository;
import com.parse.ParseObject;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import junitx.util.PrivateAccessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class ParseIncidentRepositoryTest {

  @Mock
  private ParseIncidentAdapter mockIncidentAdapter;

  @Mock
  private PhotoRepository mockPhotoRepository;

  private ParseIncidentRepository repository;
  private Incident incident;
  private RepositoryCommandResultListener<Incident> listener;

  @Before
  public void setup() throws NoSuchFieldException {
    MockitoAnnotations.initMocks(this);

    repository = new ParseIncidentRepository();

    PrivateAccessor.setField(repository, "parseIncidentAdapter", mockIncidentAdapter);
    PrivateAccessor.setField(repository, "photoRepository", mockPhotoRepository);

    incident = new Incident("name", System.currentTimeMillis(), "note", "cat", "subcat", Impact.LOW);

    listener = new RepositoryCommandResultListener<Incident>() {
      @Override
      public void commandComplete(RepositoryCommandResult<Incident> result) {
      }
    };
  }

  @Test
  public void shouldCallSerialiseOnAdapterWhenSaveIsCalled() {
    repository.save(listener, incident);

    Robolectric.runBackgroundTasks();

    verify(mockIncidentAdapter).serialise(notNull(ParseObject.class), eq(incident));
  }

}
