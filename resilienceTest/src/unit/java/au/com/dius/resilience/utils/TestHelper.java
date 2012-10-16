package au.com.dius.resilience.utils;

import android.content.Context;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.impl.RepositoryCommand;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;
import com.google.inject.Provider;

import java.util.List;

/**
 * @author georgepapas
 */
public class TestHelper {

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

  public static RepositoryCommandResultListener<Incident> createIncidentListener(
          final MutableBoolean callbackFlag,
          final List<Incident> resultList) {
    return new RepositoryCommandResultListener<Incident>() {
        @Override
        public void commandComplete(RepositoryCommandResult<Incident> result) {
          callbackFlag.setBool(result.isSuccess());
          resultList.addAll(result.getResults());
        }
      };
  }

  public static Provider<Context> createContextProvider(final Context context) {
    return new Provider<Context>() {
      @Override
      public Context get() {
        return context;
      }
    };
  }

}
