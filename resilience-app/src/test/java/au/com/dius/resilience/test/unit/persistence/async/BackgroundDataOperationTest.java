package au.com.dius.resilience.test.unit.persistence.async;

import au.com.dius.resilience.persistence.async.BackgroundDataOperation;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;
import au.com.dius.resilience.persistence.repository.impl.RepositoryCommand;
import au.com.dius.resilience.test.unit.utils.MutableBoolean;
import au.com.dius.resilience.test.unit.utils.TestHelper;
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
public class BackgroundDataOperationTest {

  @Test
  public void shouldCallBackToListenerWhenCommandCompletes() {

    final MutableBoolean yahIgotCalled = new MutableBoolean(false);
    RepositoryCommandResultListener<String> resultListener = TestHelper.createCommandListener(yahIgotCalled);

    BackgroundDataOperation<String> operation = new BackgroundDataOperation<String>();
    operation.execute(resultListener, TestHelper.createRepositoryCommand());

    Robolectric.runBackgroundTasks();
    assertTrue(yahIgotCalled.isTrue());
  }

  @Test
  public void shouldCallPerformOnCommand() {
    final MutableBoolean yayIgotCalled = new MutableBoolean(false);
    RepositoryCommandResultListener<String> resultListener = TestHelper.createCommandListener(yayIgotCalled);

    BackgroundDataOperation<String> operation = new BackgroundDataOperation<String>();

    RepositoryCommand<String> mockCommand = mock(RepositoryCommand.class);
    operation.execute(resultListener, mockCommand);

    Robolectric.runBackgroundTasks();
    verify(mockCommand).perform();
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionWhenListenerIsNull() {
    BackgroundDataOperation<String> operation = new BackgroundDataOperation<String>();
    operation.execute(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionWhenCommandIsNull() {
    BackgroundDataOperation<String> operation = new BackgroundDataOperation<String>();
    operation.execute(TestHelper.createCommandListener(null), null);
  }

}
