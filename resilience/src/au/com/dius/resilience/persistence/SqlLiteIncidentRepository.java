package au.com.dius.resilience.persistence;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.model.ImpactScale;
import au.com.dius.resilience.model.Incident;

public class SqlLiteIncidentRepository extends AbstractSqlLiteRepository<Incident> {

  public SqlLiteIncidentRepository(Context context) {
    super(context);
  }
  
  @Override
  public boolean save(Incident incident) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(COL_ID, incident.getId());
    contentValues.put(Constants.COL_INCIDENT_NAME, incident.getName());
    contentValues.put(Constants.COL_INCIDENT_CATEGORY, incident.getCategory());
    contentValues.put(Constants.COL_INCIDENT_SUBCATEGORY, incident.getSubCategory());
    contentValues.put(Constants.COL_INCIDENT_IMPACT, incident.getImpact().name());
    contentValues.put(Constants.COL_INCIDENT_CREATION_DATE, incident.getDateCreated());
    contentValues.put(Constants.COL_INCIDENT_NOTE, incident.getNote());
    
    long saved = getWritableDatabase().insertOrThrow(Constants.TABLE_INCIDENT, null, contentValues);
    close();

    return saved > 0;
  }

  @Override
  public Incident findById(long id) {
    
    Cursor cursor = getReadableDatabase().query(Constants.TABLE_INCIDENT,
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
    Cursor cursor = getReadableDatabase().query(Constants.TABLE_INCIDENT,
        null, null, null, null, null, Constants.COL_INCIDENT_CREATION_DATE + " DESC");
    
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
    String id = String.valueOf(cursor.getLong(0));
    String name = cursor.getString(1);
    String category = cursor.getString(2);
    String subCategory = cursor.getString(3);
    ImpactScale impact = ImpactScale.valueOf(cursor.getString(4));
    Long dateCreated = cursor.getLong(5);
    String note = cursor.getString(6);
    
    return new Incident(id, name, dateCreated, note, category, subCategory, impact);
  }
}
