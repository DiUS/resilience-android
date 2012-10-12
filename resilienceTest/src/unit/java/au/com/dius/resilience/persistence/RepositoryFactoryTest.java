package au.com.dius.resilience.persistence;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.content.Context;
import au.com.dius.resilience.RuntimeProperties;

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
    PowerMockito.when(RuntimeProperties.useLiveDb()).thenReturn(true);
    Assert.assertTrue(RepositoryFactory.createIncidentRepository(context) instanceof ParseRepository);
  }

  @Test
  public void testShouldReturnSqlLiteInstanceWhenNotInLiveMode() {
    PowerMockito.when(RuntimeProperties.useLiveDb()).thenReturn(false);
    Assert.assertTrue(RepositoryFactory.createIncidentRepository(context) instanceof SqlLiteRepository);
  }
}