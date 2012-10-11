package au.com.dius.resilience.persistence;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import au.com.dius.resilience.model.Incident;

public class SqlLiteRepository extends SQLiteOpenHelper implements Repository {

  private static final int DATABASE_VERSION = 1;
  
  public static final String DB_NAME = "resilience.db";
  public static final String TABLE_INCIDENT = "incident";
  
  public static final String COL_ID = "_id";
  public static final String COL_NAME = "name";
  public static final String COL_CREATION_DATE = "dateCreated";
  public static final String COL_NOTE = "note";
  
  public static final String CREATE_DB_SQL = "CREATE TABLE " + TABLE_INCIDENT
                                            + " (" + COL_ID + " integer primary key autoincrement, "
                                            + COL_NAME + " text not null, "
                                            + COL_CREATION_DATE + " date not null, "
                                            + COL_NOTE + " text);";
  
  public SqlLiteRepository(Context context) {
    super(context, DB_NAME, null, DATABASE_VERSION);
  }
  
  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_DB_SQL);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(SqlLiteRepository.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", old data will be pulverised");
    db.execSQL("DROP TABLE IF EXISTS " + COL_NOTE);
    onCreate(db);
  }

  @Override
  public void save(Incident incident) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(COL_ID, incident.getId());
    contentValues.put(COL_NAME, incident.getName());
    contentValues.put(COL_CREATION_DATE, incident.getDateCreated());
    contentValues.put(COL_NOTE, incident.getNote());
    
    getWritableDatabase().insertOrThrow(TABLE_INCIDENT, null, contentValues);
    close();
  }

  @Override
  public Incident findById(long id) {
    
    Cursor cursor = getReadableDatabase().query(TABLE_INCIDENT,
        null, "WHERE _id = + " + id, null, null, null, null);
    
    if (cursor.getCount() > 1) {
      throw new RuntimeException("Query returned " + cursor.getCount() + ", expected 0 or 1.");
    }
    
    cursor.moveToFirst();
    Incident incident = cursorToIncident(cursor);
    
    close();
    
    return incident;
  }

  @Override
  public List<Incident> findAll() {
    List<Incident> allIncidents = new ArrayList<Incident>();

    // FIXME - passing null as the second parameter returns all columns.
    // If we only want specific data later this could be optimised.
    Cursor cursor = getReadableDatabase().query(TABLE_INCIDENT,
        null, null, null, null, null, null);
    
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Incident incident = cursorToIncident(cursor);
      allIncidents.add(incident);
      cursor.moveToNext();
    }
    cursor.close();
    
    return allIncidents;
  }

  private Incident cursorToIncident(Cursor cursor) {
    Long id = cursor.getLong(0);
    String name = cursor.getString(1);
    Long dateCreated = cursor.getLong(2);
    String note = cursor.getString(3);
    
    return new Incident(id, name, dateCreated, note);
  }
}
