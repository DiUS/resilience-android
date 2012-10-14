package au.com.dius.resilience.persistence;

import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.content.Context;
import au.com.dius.resilience.RuntimeProperties;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RuntimeProperties.class, RepositoryFactory.class})
public class RepositoryFactoryTest {

  private RepositoryFactory repositoryFactory;
  private Context context;

  @Before
  public void setUp() throws Exception {

    repositoryFactory = new RepositoryFactory();
    MockitoAnnotations.initMocks(this);

    PowerMockito.mockStatic(RuntimeProperties.class);
    context = PowerMockito.mock(Context.class);
    PowerMockito.whenNew(SqlLiteIncidentRepository.class).withArguments(context).thenReturn(Mockito.mock(SqlLiteIncidentRepository.class));
  }
  
  @Test
  public void testShouldReturnParseRepositoryWhenInLiveDbMode() {
    Mockito.when(RuntimeProperties.useLiveDb()).thenReturn(true);
    assertTrue(repositoryFactory.createIncidentRepository(context) instanceof ParseIncidentRepository);
  }

  @Test
  public void testShouldReturnSqlLiteInstanceWhenNotInLiveMode() {
    PowerMockito.when(RuntimeProperties.useLiveDb()).thenReturn(false);
    assertTrue(repositoryFactory.createIncidentRepository(context) instanceof SqlLiteIncidentRepository);
  }
}
