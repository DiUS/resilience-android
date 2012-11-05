package au.com.dius.resilience.test.util;

import android.app.Instrumentation;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.R;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ParseTestUtils {

  public static final String LOG_TAG = ParseTestUtils.class.getName();

  public static void setUp(Context context) {
    Parse.initialize(
      context,
      context.getString(R.string.key_parse_application),
      context.getString(R.string.key_parse_client));

    Parse.setLogLevel(Parse.LOG_LEVEL_INFO);
  }

  public static void dropAll(Instrumentation instrumentation) {
    instrumentation.runOnMainSync(new DropTables());
    instrumentation.waitForIdleSync();
  }

  private static class DropTables implements Runnable {

    public void run() {

      StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
      try {
        ParseQuery query = new ParseQuery(Constants.TABLE_INCIDENT);
        List<ParseObject> incidents = query.find();

        if (incidents == null || incidents.size() == 0) {
          return;
        }

        Log.d(LOG_TAG, "Deleting " + incidents.size() + " incidents.");

        for (ParseObject pObject : incidents) {
          Log.d(LOG_TAG, "Deleting object " + pObject.getObjectId() + " succeeded.");
          pObject.delete();
        }
      } catch (Exception e) {
        throw new RuntimeException("Failed to delete objects: ", e);
      }
      finally {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
      }
    }
  }
}
