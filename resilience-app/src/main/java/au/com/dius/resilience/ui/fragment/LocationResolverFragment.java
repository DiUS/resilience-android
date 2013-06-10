package au.com.dius.resilience.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import au.com.dius.resilience.R;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.location.LocationBroadcaster;
import au.com.dius.resilience.ui.ViewStateManager;
import au.com.dius.resilience.ui.fragment.state.LocationNotResolvedViewState;
import au.com.dius.resilience.ui.fragment.state.LocationResolvedViewState;
import com.google.inject.Inject;
import roboguice.fragment.RoboFragment;

public class LocationResolverFragment extends RoboFragment {

  public static final String STATE_LOCATION_RESOLVED = "STATE_LOCATION_RESOLVED";
  public static final String STATE_LOCATION_NOT_RESOLVED = "STATE_LOCATION_NOT_RESOLVED";

  private ViewStateManager viewStateManager;

  @Inject
  private LocationBroadcaster locationBroadcaster;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.location_resolver_fragment, container);

    viewStateManager = new ViewStateManager(view);
    viewStateManager.registerState(STATE_LOCATION_RESOLVED, new LocationResolvedViewState());
    viewStateManager.registerState(STATE_LOCATION_NOT_RESOLVED, new LocationNotResolvedViewState());

    viewStateManager.swap(STATE_LOCATION_NOT_RESOLVED, null);

    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    locationBroadcaster.startPolling();
    getActivity().registerReceiver(new LocationResolverFragmentReceiver(), new IntentFilter(Intents.RESILIENCE_LOCATION_UPDATED));
  }

  private class LocationResolverFragmentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      viewStateManager.swap(STATE_LOCATION_RESOLVED, intent);
      locationBroadcaster.stopPolling();
    }
  }
}