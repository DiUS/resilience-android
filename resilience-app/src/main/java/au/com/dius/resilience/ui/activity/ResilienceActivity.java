package au.com.dius.resilience.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import au.com.dius.resilience.R;
import au.com.dius.resilience.actionbar.ActionBarHandler;
import au.com.dius.resilience.location.LocationBroadcaster;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;
import au.com.dius.resilience.ui.ResilienceActionBarThemer;
import au.com.dius.resilience.util.Logger;
import com.google.inject.Inject;
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

  @Inject
  private LocationBroadcaster locationBroadcaster;

  @Inject
  private PreferenceAdapter preferenceAdapter;

  @Inject
  private ActionBarHandler actionBarHandler;

  @Inject
  private ResilienceActionBarThemer actionBar;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setupTabs();
    locationBroadcaster.startPolling();
  }

  private void setupTabs() {
    tabHost.setup();

    tabHost.addTab(newTab(TAB_TAG_LIST_VIEW, getResources().getString(R.string.label_tab_list_view), ServiceRequestListActivity.class));
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
    return actionBarHandler.handleMenuItemSelected(item);
  }

  @Override
  public void onTabChanged(String tabTag) {
    Logger.d(this, "onTabChanged(): tabId=", tabTag);

    restyleTabs();

    currentTabTag = tabTag;
  }

  // TODO - Now that the map is a fragment, we should be able to use the standard
  // actionbar tabs, hoowee!
  private void restyleTabs() {
    // Each tab needs to have their own selector fo their individual states. Nothing else
    // seems to work.
    for (int i = 0; i < tabHost.getTabWidget().getTabCount(); ++i) {
      View nextTab = tabHost.getTabWidget().getChildTabViewAt(i);
      if (i == getTabHost().getCurrentTab()) {
        nextTab.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tab_selected));
      }
      else {
        nextTab.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tab_unselected));
      }
    }
  }
}
