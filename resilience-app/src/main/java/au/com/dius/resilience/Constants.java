package au.com.dius.resilience;

public class Constants {
  public static final String TABLE_INCIDENT = "incident";
  public static final String COL_INCIDENT_NAME = "name";
  public static final String COL_INCIDENT_CATEGORY = "category";
  public static final String COL_INCIDENT_SUBCATEGORY = "subcategory";
  public static final String COL_INCIDENT_IMPACT = "impact";
  public static final String COL_INCIDENT_CREATION_DATE = "createdAt";
  public static final String COL_INCIDENT_NOTE = "note";
  public static final String COL_INCIDENT_PHOTO = "incident_photo";
  public static final String COL_INCIDENT_LOCATION = "location";
  public static final String PHOTO_FILENAME = COL_INCIDENT_PHOTO + ".jpg";

  public static final String EXTRA_PHOTO = "photo";

  public static final String INCIDENT_POINT = "incidentPoint";

  public static final String PREFERENCES_FILE_PREFIX = "au.com.dius.resilience.preferences.";
  public static final String PREFERENCES_FILE_COMMON = PREFERENCES_FILE_PREFIX + "common";
}