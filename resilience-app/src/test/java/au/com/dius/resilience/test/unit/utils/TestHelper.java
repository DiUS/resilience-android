package au.com.dius.resilience.test.unit.utils;

import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;
import au.com.dius.resilience.persistence.repository.impl.RepositoryCommand;
import junitx.util.PrivateAccessor;

import java.util.ArrayList;

public class TestHelper {
  public static RepositoryCommand<String> createRepositoryCommand() {
    return new RepositoryCommand<String>() {
      public RepositoryCommandResult<String> perform() {
        return new RepositoryCommandResult<String>(true, new ArrayList<String>());
      }
    };
  }

  public static RepositoryCommandResultListener<String> createCommandListener(final MutableBoolean callbackFlag) {
    return new RepositoryCommandResultListener<String>() {
        public void commandComplete(RepositoryCommandResult<String> result) {
          callbackFlag.setBool(true);
        }
      };
  }

  public static Object getField(Object target, String fieldName) {
    try {
      return PrivateAccessor.getField(target, fieldName);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException("Failed to get field '" + fieldName + "'" + " on " + target);
    }
  }

  public static void setField(Object target, String fieldName, Object value) {
    try {
      PrivateAccessor.setField(target, fieldName, value);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException("Failed to set field '" + fieldName + "'" + " on " + target);
    }
  }
}
