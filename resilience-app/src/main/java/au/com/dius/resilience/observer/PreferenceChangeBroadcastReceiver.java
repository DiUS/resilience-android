package au.com.dius.resilience.observer;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Executes method on given PreferenceChangeListener. Useful when
 * wanting to receive broadcasts on a class without extending
 * BroadcastReceiver.
 */
public class PreferenceChangeBroadcastReceiver extends BroadcastReceiver {

  public static final String PREFERENCES_UPDATED_FILTER = "PREFERENCES_UPDATED_FILTER";

  private PreferenceChangeListener listener;

  public PreferenceChangeBroadcastReceiver(PreferenceChangeListener listener) {
    this.listener = listener;
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    listener.onPreferenceChange();
  }
}
