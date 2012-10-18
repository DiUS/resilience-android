package au.com.dius.resilience.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import au.com.dius.resilience.R;
import au.com.dius.resilience.ui.Codes;
import roboguice.activity.RoboTabActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.main)
public class ResilienceActivity extends RoboTabActivity implements TabHost.OnTabChangeListener {

  private static final String LOG_TAG = "ResilienceActivity";
  private static final String TAB_TAG_LIST_VIEW = "list_view";
  private static final String TAB_TAG_MAP_VIEW = "map_view";

  @InjectView(android.R.id.tabhost)
  private TabHost tabHost;

  private String currentTabTag;

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    //TODO Restore from savedInstance state

    super.onCreate(savedInstanceState);
    setupTabs();
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

}
