package au.com.dius.resilience.persistence;

import android.content.Context;
import au.com.dius.resilience.RuntimeProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RuntimeProperties.class, RepositoryFactory.class})
public class RepositoryFactoryTest {

  private Context context;

  @Before
  public void setUp() throws Exception {
    PowerMockito.mockStatic(RuntimeProperties.class);
    context = PowerMockito.mock(Context.class);
    PowerMockito.whenNew(SqlLiteRepository.class).withArguments(context).thenReturn(Mockito.mock(SqlLiteRepository.class));
  }
  
  @Test
  public void testShouldReturnParseRepositoryWhenInLiveDbMode() {
    Mockito.when(RuntimeProperties.useLiveDb()).thenReturn(true);
    assertTrue(RepositoryFactory.createIncidentRepository(context) instanceof ParseRepository);
  }

  @Test
  public void testShouldReturnSqlLiteInstanceWhenNotInLiveMode() {
    PowerMockito.when(RuntimeProperties.useLiveDb()).thenReturn(false);
    assertTrue(RepositoryFactory.createIncidentRepository(context) instanceof SqlLiteRepository);
  }
}
