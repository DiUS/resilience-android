package au.com.dius.resilience;

public class Constants {
  public static final String COL_ID = "objectId";
  
  public static final String TABLE_INCIDENT = "incident";
  public static final String COL_INCIDENT_NAME = "name";
  public static final String COL_INCIDENT_CATEGORY = "category";
  public static final String COL_INCIDENT_SUBCATEGORY = "subcategory";
  public static final String COL_INCIDENT_IMPACT = "impact";
  public static final String COL_INCIDENT_CREATION_DATE = "createdAt";
  public static final String COL_INCIDENT_NOTE = "note";
  public static final String COL_INCIDENT_PHOTO = "incident_photo";
  public static final String PHOTO_FILENAME = COL_INCIDENT_PHOTO + ".jpg";
  
  public static final String COL_PHOTO_URI = "uri";
  public static final String COL_PHOTO_DATA = "data";
  
  // FIXME - We need a way to make these keys private (these keys are just junk, btw!)
  public static final String TEST_APP_KEY = "RiO29avl2HCTX49CphzVrpRKawqUFSUJbHtZRitM";
  public static final String TEST_CLIENT_KEY = "GO9aRT96Wmht7x6ztSTnm4CXNJcd46vSZcaIA9Jm";


}