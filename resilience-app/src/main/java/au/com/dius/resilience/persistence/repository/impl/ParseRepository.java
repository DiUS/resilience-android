package au.com.dius.resilience.persistence.repository.impl;

import android.util.Log;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.Repository;
import com.google.inject.Inject;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import roboguice.inject.ContextSingleton;

import java.util.ArrayList;
import java.util.List;

@ContextSingleton
public class ParseRepository implements Repository {

  private static final String TAG = ParseRepository.class.getName();

  final ParseIncidentAdapter incidentAdapter;

  @Inject
  public ParseRepository(ParseIncidentAdapter incidentAdapter) {
    this.incidentAdapter = incidentAdapter;
  }

  @Override
  public List<Incident> findIncidents() {
    ParseQuery query = new ParseQuery(Constants.TABLE_INCIDENT);
    List<ParseObject> parseObjects = loadIncidents(query);

    List<Incident> incidents = new ArrayList<Incident>();
    for (ParseObject parseIncident : parseObjects) {
      incidents.add(incidentAdapter.deserialise(parseIncident));
    }

    return incidents;
  }

  private List<ParseObject> loadIncidents(ParseQuery query) {
    try {
      return query.find();
    } catch (ParseException e) {
      Log.d(TAG, "Loading all incidents failed with: ", e);
      throw new RuntimeException(e);
    }
  }
}
