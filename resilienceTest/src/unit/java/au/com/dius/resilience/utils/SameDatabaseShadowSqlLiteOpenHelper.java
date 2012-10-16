package au.com.dius.resilience.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.internal.RealObject;

/**
 * @author georgepapas
 */
@Implements(SQLiteOpenHelper.class)
public class SameDatabaseShadowSqlLiteOpenHelper  {

  @RealObject
  private SQLiteOpenHelper realHelper;
  private static SQLiteDatabase database;

  public void __constructor__(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
      if (database != null) {
          database.close();
      }
      database = null;
  }

  @Implementation
  public synchronized void close() {
//      if (database != null) {
//          database.close();
//      }
//      database = null;
  }

  @Implementation
  public synchronized SQLiteDatabase getReadableDatabase() {
      if (database == null) {
          database = SQLiteDatabase.openDatabase("path", null, 0);
          realHelper.onCreate(database);
      }

      realHelper.onOpen(database);
      return database;
  }

  @Implementation
  public synchronized SQLiteDatabase getWritableDatabase() {
      if (database == null) {
          database = SQLiteDatabase.openDatabase("path", null, 0);
          realHelper.onCreate(database);
      }

      realHelper.onOpen(database);
      return database;
  }
}
