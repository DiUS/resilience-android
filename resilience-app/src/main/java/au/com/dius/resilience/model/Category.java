package au.com.dius.resilience.model;

@Deprecated
public enum Category {
  UNKNOWN("unknown"),
  FIRE("fire"),
  FLOOD("flood"),
  POLLUTION("pollution"),
  LIVESTOCK("livestock"),
  WILDLIFE("wildlife"),
  ROADS("roads"),
  ELECTRICITY("electricity"),
  GAS("gas"),
  WATER("water"),
  DRAINAGE("drainage"),
  FENCING("fencing");

  private String imageFilename;

  private Category(String imageFilename) {
   this.imageFilename = imageFilename;
  }

  public String getImageFilename() {
    return imageFilename;
  }

  public static Category asCategory(String str) {
    for (Category cat : Category.values()) {
      if (cat.getImageFilename().equalsIgnoreCase(str))
        return cat;
    }
    return UNKNOWN;
  }
}
