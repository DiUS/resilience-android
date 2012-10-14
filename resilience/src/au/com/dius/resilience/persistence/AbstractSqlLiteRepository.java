package au.com.dius.resilience.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class AbstractSqlLiteRepository<T> extends SQLiteOpenHelper implements Repository<T> {

  protected static final int DATABASE_VERSION = 1;
  public static final String DB_NAME = "resilience.db";
  
  public static final String TABLE_INCIDENT = "incident";
  
  public static final String COL_ID = "_id";
  public static final String COL_NAME = "name";
  public static final String COL_CATEGORY = "category";
  public static final String COL_SUBCATEGORY = "subcategory";
  public static final String COL_IMPACT = "impact";
  public static final String COL_CREATION_DATE = "dateCreated";
  public static final String COL_NOTE = "note";
  
  private static final String CREATE_DB_SQL = "CREATE TABLE " + TABLE_INCIDENT
                                            + " (" + COL_ID + " integer primary key autoincrement, "
                                            + COL_NAME + " text, "
                                            + COL_CATEGORY + " text not null, "
                                            + COL_SUBCATEGORY + " text not null, "
                                            + COL_IMPACT + " text not null, "
                                            + COL_CREATION_DATE + " date not null, "
                                            + COL_NOTE + " text);";
  
  public AbstractSqlLiteRepository(Context context) {
    super(context, DB_NAME, null, DATABASE_VERSION);
  }
  
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(SqlLiteIncidentRepository.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", old data will be pulverised");
    db.execSQL("DROP TABLE IF EXISTS " + COL_NOTE);
    onCreate(db);
  }
  
  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_DB_SQL);
  }
}
