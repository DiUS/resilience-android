package au.com.dius.resilience.persistence.repository.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.model.ImpactScale;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.IncidentRepository;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;
import com.google.inject.Inject;
import com.google.inject.Provider;
import roboguice.inject.ContextSingleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ContextSingleton
public class SqlLiteIncidentRepository extends AbstractSqlLiteRepository<Incident> implements IncidentRepository {

  @Inject
  public SqlLiteIncidentRepository(Provider<Context> contextProvider) {
    super(contextProvider.get());
  }

  @Override
  public void save(RepositoryCommandResultListener<Incident> listener, final Incident incident) {
    RepositoryCommand<Incident> command = new RepositoryCommand<Incident>() {
      @Override
      public RepositoryCommandResult<Incident> perform() {
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

        return new RepositoryCommandResult<Incident>(saved > 0, Collections.EMPTY_LIST);
      }
    };

    executeInBackground(listener, command);
  }

  @Override
  public void findById(RepositoryCommandResultListener<Incident> listener, final String id) {
    RepositoryCommand<Incident> command = new RepositoryCommand<Incident>() {
      @Override
      public RepositoryCommandResult<Incident> perform() {
        Cursor cursor = getReadableDatabase().query(Constants.TABLE_INCIDENT,
                null, "WHERE _id = + " + id, null, null, null, null);

        if (cursor.getCount() > 1) {
          throw new RuntimeException("Query returned " + cursor.getCount() + ", expected 0 or 1.");
        }

        cursor.moveToFirst();
        Incident incident = cursorToIncident(cursor);

        close();
        return new RepositoryCommandResult<Incident>(incident != null, Arrays.asList(new Incident[]{incident}));
      }
    };

    executeInBackground(listener, command);
  }

  @Override
  public void findAll(RepositoryCommandResultListener<Incident> listener) {
    RepositoryCommand<Incident> command = new RepositoryCommand<Incident>() {
      @Override
      public RepositoryCommandResult<Incident> perform() {
        List<Incident> allIncidents = new ArrayList<Incident>();

        // FIXME - passing null as the second parameter returns all columns.
        // If we only want specific data later this could be optimised.
        Cursor cursor = getReadableDatabase().query(Constants.TABLE_INCIDENT,
                null, null, null, null, null, Constants.COL_INCIDENT_CREATION_DATE + " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
          allIncidents.add(cursorToIncident(cursor));
          cursor.moveToNext();
        }
        cursor.close();

        return new RepositoryCommandResult<Incident>(true, allIncidents);
      }
    };

    executeInBackground(listener, command);
  }

  @Override
  public void findClosest(RepositoryCommandResultListener<Incident> listener) {
    throw new UnsupportedOperationException("Not implemented in sql lite");
  }

  @Override
  public void findTracked(RepositoryCommandResultListener<Incident> listener) {
    throw new UnsupportedOperationException("Not implemented in sql lite");
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
