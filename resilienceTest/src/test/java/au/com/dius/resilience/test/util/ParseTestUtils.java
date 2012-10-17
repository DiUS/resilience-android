package au.com.dius.resilience.test.util;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;
import au.com.dius.resilience.Constants;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ParseTestUtils {
  public static void setUp(Context context) {
    Parse
        .initialize(context, Constants.TEST_APP_KEY, Constants.TEST_CLIENT_KEY);
  }

  public static void dropAll(Instrumentation instrumentation) {
    instrumentation.runOnMainSync(new DropTables());
    instrumentation.waitForIdleSync();
  }

  private static class DropTables implements Runnable {
    @Override
    public void run() {
      try {
        ParseQuery query = new ParseQuery(Constants.TABLE_INCIDENT);
        Log.d(ParseTestUtils.class.getName(), "Finding list of objects..");
        query.findInBackground(new FindCallback() {
          @Override
          public void done(List<ParseObject> incidents, ParseException ex) {
            if (ex != null || incidents.size() == 0) {
              return;
            }

            try {
              deleteObjects(incidents);
            }
            catch(Exception e) {
              Log.e(DropTables.class.getName(), "Failed to delete objects!");
            }
          }

          private void deleteObjects(List<ParseObject> parseObjects) throws InterruptedException {
            final CountDownLatch deleteLatch = new CountDownLatch(parseObjects.size());
            for (ParseObject i : parseObjects) {
              i.deleteInBackground(new DeleteCallback() {
                @Override
                public void done(ParseException ex) {
                  deleteLatch.countDown();
                }
              });
            }

            Log.d(ParseTestUtils.class.getName(), "Clearing data..");
            deleteLatch.await(15, TimeUnit.SECONDS);
          }
        });

      } catch (Exception e) {
        throw new RuntimeException("Test setup failed: ", e);
      }
    }
  }
}
