package au.com.dius.resilience.model;

import android.content.Context;
import android.telephony.TelephonyManager;

public class Device {

  public static String getDeviceId(Context context) {
    return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
  }
}
