package au.com.dius.resilience.shadow;

import android.app.LoaderManager;
import android.widget.ListView;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.shadows.ShadowListActivity;
import roboguice.activity.RoboListActivity;

import static org.mockito.Mockito.mock;

@SuppressWarnings({"UnusedDeclaration"})
@Implements(RoboListActivity.class)
public class ResilienceShadowActivity extends ShadowListActivity {

  private LoaderManager loaderManager;
  private ListView listView;

  @Implementation
  public LoaderManager getLoaderManager() {

    if (loaderManager == null) {
      loaderManager = mock(LoaderManager.class);
    }

    return loaderManager;
  }

  @Implementation
  public ListView getListView() {
    if (listView == null) {
      listView = mock(ListView.class);
    }

    return listView;
  }
}