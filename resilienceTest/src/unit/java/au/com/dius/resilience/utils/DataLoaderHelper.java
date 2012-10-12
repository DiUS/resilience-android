package au.com.dius.resilience.utils;

import au.com.dius.resilience.persistence.RepositoryCommand;
import au.com.dius.resilience.persistence.RepositoryCommandResult;
import au.com.dius.resilience.persistence.RepositoryCommandResultListener;

/**
 * @author georgepapas
 */
public class DataLoaderHelper {

  public static RepositoryCommand<String> createRepositoryCommand() {
    return new RepositoryCommand<String>() {
      @Override
      public RepositoryCommandResult<String> perform() {
        return new RepositoryCommandResult<String>(true, null);
      }
    };
  }

  public static RepositoryCommandResultListener<String> createCommandListener(final MutableBoolean callbackFlag) {
    return new RepositoryCommandResultListener<String>() {
        @Override
        public void commandComplete(RepositoryCommandResult<String> result) {
          callbackFlag.setBool(true);
        }
      };
  }

}
