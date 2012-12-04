package au.com.dius.resilience.model;

import android.content.Context;
import android.telephony.TelephonyManager;

public class Device {

  // TODO - Bleh, could be better.
  public static String getDeviceId(Context context) {
    return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
  }
}
