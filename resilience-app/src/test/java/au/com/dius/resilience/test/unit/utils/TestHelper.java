package au.com.dius.resilience.test.unit.utils;

import android.content.Context;
import android.content.Intent;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;
import au.com.dius.resilience.persistence.repository.impl.RepositoryCommand;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import junit.framework.Assert;
import junitx.util.PrivateAccessor;
import roboguice.RoboGuice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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

  public static RepositoryCommandResultListener<Incident> createIncidentListener(
      final MutableBoolean callbackFlag,
      final List<Incident> resultList, final CountDownLatch... latch) {
    return new RepositoryCommandResultListener<Incident>() {

      public void commandComplete(RepositoryCommandResult<Incident> result) {
        callbackFlag.setBool(result.isSuccess());
        resultList.addAll(result.getResults());

        if (latch != null) {
          latch[0].countDown();
        }
      }
    };
  }

  public static RepositoryCommandResultListener<Photo> createPhotoListener(
      final MutableBoolean callbackFlag,
      final List<Photo> resultList, final CountDownLatch... latch) {
    return new RepositoryCommandResultListener<Photo>() {

      public void commandComplete(RepositoryCommandResult<Photo> result) {
        callbackFlag.setBool(result.isSuccess());
        resultList.addAll(result.getResults());

        if (latch != null) {
          latch[0].countDown();
        }
      }
    };
  }

  public static Provider<Context> createContextProvider(final Context context) {
    return new Provider<Context>() {
      public Context get() {
        return context;
      }
    };
  }

  public static void assertContainsIntents(List<Intent> intents, String intentAction) {
    for (Intent intent : intents) {
      if (intentAction.equals(intent.getAction())) {
        return;
      }
    }
    Assert.fail("Did not find intent in list " + intentAction);
  }

  public static void overrideRoboguiceModule(Module module) {
    RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE, Modules.override(
      RoboGuice.newDefaultRoboModule(Robolectric.application)).with(module));
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
