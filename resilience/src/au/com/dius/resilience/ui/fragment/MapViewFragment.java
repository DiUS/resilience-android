package au.com.dius.resilience.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import au.com.dius.resilience.R;
import roboguice.fragment.RoboFragment;

/**
 * @author georgepapas
 */
public class MapViewFragment extends RoboFragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_map_view, container, false);
  }
}
