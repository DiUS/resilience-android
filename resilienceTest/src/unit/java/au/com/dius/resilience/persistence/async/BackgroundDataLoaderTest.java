package au.com.dius.resilience.persistence.async;

import au.com.dius.resilience.persistence.RepositoryCommand;
import au.com.dius.resilience.persistence.RepositoryCommandResultListener;
import au.com.dius.resilience.utils.DataLoaderHelper;
import au.com.dius.resilience.utils.MutableBoolean;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author georgepapas
 */
@RunWith(RobolectricTestRunner.class)
public class BackgroundDataLoaderTest {

  @Test
  public void shouldCallBackToListenerWhenCommandCompletes() {

    final MutableBoolean yahIgotCalled = new MutableBoolean(false);
    RepositoryCommandResultListener<String> resultListener = DataLoaderHelper.createCommandListener(yahIgotCalled);

    BackgroundDataLoader<String> loader = new BackgroundDataLoader<String>();
    loader.execute(resultListener, DataLoaderHelper.createRepositoryCommand());

    Robolectric.runBackgroundTasks();
    assertTrue(yahIgotCalled.isTrue());
  }

  @Test
  public void shouldCallPerformOnCommand() {
    final MutableBoolean yayIgotCalled = new MutableBoolean(false);
    RepositoryCommandResultListener<String> resultListener = DataLoaderHelper.createCommandListener(yayIgotCalled);

    BackgroundDataLoader<String> loader = new BackgroundDataLoader<String>();

    RepositoryCommand<String> mockCommand = mock(RepositoryCommand.class);
    loader.execute(resultListener, mockCommand);

    Robolectric.runBackgroundTasks();
    verify(mockCommand).perform();
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionWhenListenerIsNull() {
    BackgroundDataLoader<String> loader = new BackgroundDataLoader<String>();
    loader.execute(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionWhenCommandIsNull() {
    BackgroundDataLoader<String> loader = new BackgroundDataLoader<String>();
    loader.execute(DataLoaderHelper.createCommandListener(null), null);
  }

}
