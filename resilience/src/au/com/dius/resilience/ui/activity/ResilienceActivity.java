package au.com.dius.resilience.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import au.com.dius.resilience.R;
import au.com.dius.resilience.ui.Codes;
import au.com.dius.resilience.ui.fragment.IncidentListFragment;
import au.com.dius.resilience.ui.fragment.MapViewFragment;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.main)
public class ResilienceActivity extends RoboFragmentActivity implements TabHost.OnTabChangeListener {

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
    tabHost.addTab(newTab(TAB_TAG_LIST_VIEW, getResources().getString(R.string.label_tab_list_view), R.id.tab_list_view));
    tabHost.addTab(newTab(TAB_TAG_MAP_VIEW, getResources().getString(R.string.label_tab_map_view), R.id.tab_map_view));

    tabHost.setOnTabChangedListener(this);
    if (currentTabTag == null) {
      currentTabTag = TAB_TAG_LIST_VIEW;
    }
    tabHost.setCurrentTabByTag(currentTabTag);
    onTabChanged(currentTabTag);
  }

  private TabHost.TabSpec newTab(String tag, String label, int tabContentId) {
    TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
    tabSpec.setIndicator(label);
    tabSpec.setContent(tabContentId);

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

    final FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = null;
    try {
      transaction = fragmentManager.beginTransaction();
      if (TAB_TAG_LIST_VIEW.equals(tabTag)) {
        showTabContent(fragmentManager, transaction, R.id.fragment_incident_list_view, R.id.tab_list_view);
        hideTabContent(fragmentManager, transaction, R.id.fragment_incident_map_view);
      } else if (TAB_TAG_MAP_VIEW.equals(tabTag)) {
        showTabContent(fragmentManager, transaction, R.id.fragment_incident_map_view, R.id.tab_map_view);
        hideTabContent(fragmentManager, transaction, R.id.fragment_incident_list_view);
      }
    } finally {
      if (transaction != null && !transaction.isEmpty()) {
        transaction.commit();
      }
    }
  }

  private void hideTabContent(FragmentManager fragmentManager, FragmentTransaction transaction, int fragmentIdToHide) {
    Fragment fragment = fragmentManager.findFragmentById(fragmentIdToHide);
    if (fragment != null) {
      transaction.hide(fragment);
    }
  }

  private void showTabContent(
          final FragmentManager fragmentManager,
          final FragmentTransaction transaction,
          final int fragmentIdToShow,
          final int containerViewId) {

    Fragment fragment = fragmentManager.findFragmentById(fragmentIdToShow);
    if (fragment == null) {
      fragment = getFragment(fragmentIdToShow);
    }
    transaction.replace(containerViewId, fragment);
}

  private Fragment getFragment(int fragmentId) {
    Fragment fragment;

    switch (fragmentId) {
      case R.id.fragment_incident_list_view:
        fragment = new IncidentListFragment();
        break;
      case R.id.fragment_incident_map_view:
        fragment = new MapViewFragment();
        break;
      default:
        throw new IllegalStateException("Attempt to show unknown fragment with id " + fragmentId);
    }
    return fragment;
  }

}
