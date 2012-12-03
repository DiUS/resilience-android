package au.com.dius.resilience.model;

import java.io.Serializable;

public class Feedback implements Serializable {
  private String text;
  private String deviceId;

  public Feedback(String text, String deviceId) {
    this.text = text;
    this.deviceId = deviceId;
  }

  public String getText() {
    return text;
  }

  public String getDeviceId() {
    return deviceId;
  }
}
