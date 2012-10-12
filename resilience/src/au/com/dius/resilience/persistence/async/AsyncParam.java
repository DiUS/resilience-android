package au.com.dius.resilience.persistence.async;

import au.com.dius.resilience.persistence.RepositoryCommand;
import au.com.dius.resilience.persistence.RepositoryCommandResultListener;

/**
 * @author georgepapas
 */
public class AsyncParam<T> {

  private RepositoryCommandResultListener<T> listener;
  private RepositoryCommand<T> command;

  public AsyncParam(RepositoryCommandResultListener<T> listener, RepositoryCommand<T> command) {
    this.listener = listener;
    this.command = command;
  }

  public RepositoryCommandResultListener<T> getListener() {
    return listener;
  }

  public RepositoryCommand<T> getCommand() {
    return command;
  }

}
