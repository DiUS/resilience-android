package au.com.dius.resilience.test.shared.utils;

import android.content.Context;
import android.content.Intent;
import au.com.dius.resilience.bootstrap.ResilienceModule;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;
import au.com.dius.resilience.persistence.repository.impl.RepositoryCommand;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import roboguice.RoboGuice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.sun.tools.internal.ws.wsdl.parser.Util.fail;

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
    fail("Did not find intent in list " + intentAction);
  }

  public static void overrideRoboguiceModule(Module module) {
    RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE, Modules.override(
      RoboGuice.newDefaultRoboModule(Robolectric.application)).with(module));
  }
}
