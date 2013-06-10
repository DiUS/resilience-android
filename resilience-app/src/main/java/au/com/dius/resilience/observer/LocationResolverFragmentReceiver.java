package au.com.dius.resilience.observer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import au.com.dius.resilience.R;

public class LocationResolverFragmentReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    View locatingIcon = ((Activity) context).findViewById(R.id.locating_icon);
    locatingIcon.setVisibility(View.GONE);
  }
}