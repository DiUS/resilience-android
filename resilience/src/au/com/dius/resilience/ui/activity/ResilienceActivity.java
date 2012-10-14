package au.com.dius.resilience.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import au.com.dius.resilience.R;
import au.com.dius.resilience.RuntimeProperties;
import roboguice.activity.RoboFragmentActivity;

public class ResilienceActivity extends RoboFragmentActivity {

  private static final String LOG_TAG = "ResilienceActivity";

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    setStrictMode();
  }

  private void setStrictMode() {
    if (RuntimeProperties.useStrictMode()) {
      StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
              .detectDiskReads()
              .detectDiskWrites()
//            .detectNetwork()   // or .detectAll() for all detectable problems
              .detectAll()
              .penaltyLog()
              .build());

      StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
              .detectLeakedSqlLiteObjects()
              .detectLeakedClosableObjects()
              .penaltyLog()
              .penaltyDeath()
              .build());
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.action_bar, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.all_issues:
        Log.d(LOG_TAG, "All issues selected");
        break;

      case R.id.tracked_issues:
        Log.d(LOG_TAG, "Tracked issues selected");
        break;

      case R.id.raise_incident:
        Intent raiseIncident = new Intent(this, EditIncidentActivity.class);
        startActivity(raiseIncident);
        Log.d(LOG_TAG, "Raise incident selected");
        break;

      default:
        return super.onOptionsItemSelected(item);
    }
    return true;
  }
}
