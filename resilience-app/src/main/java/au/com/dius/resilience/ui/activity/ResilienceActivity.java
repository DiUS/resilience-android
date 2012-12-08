package au.com.dius.resilience.ui.activity;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import au.com.dius.resilience.R;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.model.Point;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;
import au.com.dius.resilience.ui.Codes;
import au.com.dius.resilience.ui.Themer;
import com.google.inject.Inject;
import roboguice.activity.RoboTabActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.main)
public class ResilienceActivity extends RoboTabActivity implements TabHost.OnTabChangeListener, LocationListener {

  private static final String LOG_TAG = "ResilienceActivity";
  private static final String TAB_TAG_LIST_VIEW = "list_view";
  private static final String TAB_TAG_MAP_VIEW = "map_view";

  @InjectView(android.R.id.tabhost)
  private TabHost tabHost;

  private String currentTabTag;

  //TODO Very lame implementation, will need to change to use intents and broadcast listeners
  private Point lastKnownLocation;

  @Inject
  private PreferenceAdapter preferenceAdapter;

  @Inject
  private Themer themer;

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    //TODO Restore from savedInstance state
    super.onCreate(savedInstanceState);

    setupTabs();

    setupLocationListener();
  }

  private void setupTabs() {
    tabHost.setup();
    tabHost.addTab(newTab(TAB_TAG_LIST_VIEW, getResources().getString(R.string.label_tab_list_view), IncidentListActivity.class));
    tabHost.addTab(newTab(TAB_TAG_MAP_VIEW, getResources().getString(R.string.label_tab_map_view), MapViewActivity.class));

    tabHost.setOnTabChangedListener(this);
    if (currentTabTag == null) {
      currentTabTag = TAB_TAG_LIST_VIEW;
    }
    tabHost.setCurrentTabByTag(currentTabTag);
    onTabChanged(currentTabTag);
  }

  private TabHost.TabSpec newTab(String tag, String label, Class clazz) {
    TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
    tabSpec.setIndicator(label);
    tabSpec.setContent(new Intent().setClass(this, clazz));

    return tabSpec;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.action_bar, menu);
    getActionBar().setDisplayShowTitleEnabled(false);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.all_issues:
        Log.d(LOG_TAG, "All issues selected");
        break;

      case R.id.refresh:
        Log.d(LOG_TAG, "Refreshing incidents.");
        Intent intent = new Intent(Intents.RESILIENCE_INCIDENT_CREATED);
        sendBroadcast(intent);
        break;

      case R.id.send_feedback:
        Intent sendFeedbackIntent = new Intent(Intents.RESILIENCE_FEEDBACK_REQUESTED);
        sendBroadcast(sendFeedbackIntent);
        break;
//      case R.id.tracked_issues:
//        Log.d(LOG_TAG, "Tracked issues selected");
//        break;

      case R.id.user_preferences:
        Intent userPreferencesIntent = new Intent(this, PreferenceActivity.class);
        startActivity(userPreferencesIntent);
        break;

      case R.id.create_incident:
        Intent raiseIncident = new Intent(this, EditIncidentActivity.class);
        raiseIncident.putExtra(EditIncidentActivity.LOCATION, lastKnownLocation);
        startActivityForResult(raiseIncident, Codes.CreateIncident.REQUEST_CODE);

        Log.d(LOG_TAG, "Raise incident selected");
        break;

      default:
        return super.onOptionsItemSelected(item);
    }
    return true;
  }


  @Override
  public void onTabChanged(String tabTag) {
    Log.d(LOG_TAG, "onTabChanged(): tabId=" + tabTag);
    currentTabTag = tabTag;
  }

  private void setupLocationListener() {
    LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
    for (String provider : lm.getAllProviders()) {
      lm.requestLocationUpdates(provider, 10000, 0, this);
    }
  }

  @Override
  public void onLocationChanged(Location location) {
    Log.d(LOG_TAG, "location changed " + location);
    lastKnownLocation = new Point(location.getLatitude(), location.getLongitude());

    preferenceAdapter.save(preferenceAdapter.getCommonPreferences()
                          , R.string.last_known_latitude_key, Double.toString(lastKnownLocation.getLatitude()));
    preferenceAdapter.save(preferenceAdapter.getCommonPreferences()
      , R.string.last_known_longtitude_key, Double.toString(lastKnownLocation.getLongitude()));

  }

  @Override
  public void onStatusChanged(String s, int i, Bundle bundle) {
    Log.d(LOG_TAG, (String.format("status changed %s - %d", s, i)));
  }

  @Override
  public void onProviderEnabled(String s) {
   Log.d(LOG_TAG, "Provider enabled " + s);
  }

  @Override
  public void onProviderDisabled(String s) {
    Log.d(LOG_TAG, "Provider disabled " + s);
  }
}
