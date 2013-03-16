package au.com.dius.resilience.persistence;

public class Columns {
  public final class Incident {
    public static final String TABLE_NAME = "incident";
    public static final String NAME = "name";
    public static final String CATEGORY = "category";
    public static final String SUBCATEGORY = "subcategory";
    public static final String IMPACT = "impact";
    public static final String CREATION_DATE = "createdAt";
    public static final String NOTE = "note";
    public static final String PHOTO = "photo";
    public static final String LOCATION = "location";
    public static final String TRACKED_BY = "trackedBy";
  }
  public final class Feedback {
    public static final String TABLE_NAME = "feedback";
    public static final String PHONE_ID = "phoneId";
    public static final String TEXT = "text";
  }
}
