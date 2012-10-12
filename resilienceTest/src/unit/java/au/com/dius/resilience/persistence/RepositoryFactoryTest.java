package au.com.dius.resilience.persistence;

import au.com.dius.resilience.RuntimeProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RuntimeProperties.class)
public class RepositoryFactoryTest {

  @Test
  public void testShouldReturnParseRepositoryWhenInLiveDbMode() {
    PowerMockito.mockStatic(RuntimeProperties.class);
    Mockito.when(RuntimeProperties.useLiveDb()).thenReturn(true);

    assertTrue(RepositoryFactory.create(null) instanceof ParseRepository);
  }

  //@Test
  public void testShouldReturnSqlLiteInstanceWhenNotInLiveMode() {
    PowerMockito.mockStatic(RuntimeProperties.class);
    Mockito.when(RuntimeProperties.useLiveDb()).thenReturn(false);

    assertTrue(RepositoryFactory.create(null) instanceof SqlLiteRepository);
  }
}
