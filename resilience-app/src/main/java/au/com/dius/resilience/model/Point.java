package au.com.dius.resilience.model;

import java.io.Serializable;

public class Point implements Serializable{

  double latitude;
  double longitude;

  public Point(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }
}
