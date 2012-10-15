package au.com.dius.resilience.test.util;

import java.util.List;

import android.content.Context;
import au.com.dius.resilience.Constants;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ParseTestUtils {
  public static void setUp(Context context) {
    Parse.initialize(context, Constants.TEST_APP_KEY, Constants.TEST_CLIENT_KEY);

    try {
      ParseQuery query = new ParseQuery(Constants.TABLE_INCIDENT);
      List<ParseObject> incidentList = query.find();
      for (ParseObject i : incidentList) {
        i.delete();
      }
    }
    catch(Exception e) {
      throw new RuntimeException("Test setup failed: ", e);
    }
  }
}
