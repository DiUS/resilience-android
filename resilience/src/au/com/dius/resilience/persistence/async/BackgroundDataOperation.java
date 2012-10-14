package au.com.dius.resilience.persistence.async;

import android.os.AsyncTask;
import au.com.dius.resilience.persistence.RepositoryCommand;
import au.com.dius.resilience.persistence.RepositoryCommandResult;
import au.com.dius.resilience.persistence.RepositoryCommandResultListener;

/**
 * @author georgepapas
 *
 * Executes the given {@link RepositoryCommand} in a background thread.  Once the command completes calls the
 * {@link RepositoryCommandResultListener#commandComplete(au.com.dius.resilience.persistence.RepositoryCommandResult)}
 * method in the UI Thread.
 */
public class BackgroundDataOperation<T> {

  public void execute(RepositoryCommandResultListener<T> listener, RepositoryCommand<T> command) {

    argCheck(listener, command);

    AsyncParam<T> asyncParam = new AsyncParam<T>(listener, command);
    AsyncTask<AsyncParam, Integer, RepositoryCommandResult<T>> asyncLoader = new AsyncTask<AsyncParam, Integer, RepositoryCommandResult<T>>() {

      private RepositoryCommandResultListener<T> listener;

      @Override
      protected RepositoryCommandResult<T> doInBackground(AsyncParam... params) {
        this.listener = params[0].getListener();
        return params[0].getCommand().perform();
      }

      @Override
      protected void onPostExecute(RepositoryCommandResult<T> result) {
        listener.commandComplete(result);
      }
    };

    asyncLoader.execute(asyncParam);
  }

  private void argCheck(Object listener, Object command) {
    if (listener == null || command == null) {
      throw new IllegalArgumentException("listener cannot be null");
    }
  }

}
