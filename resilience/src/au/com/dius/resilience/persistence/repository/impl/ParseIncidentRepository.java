package au.com.dius.resilience.persistence.repository.impl;

import au.com.dius.resilience.Constants;
import au.com.dius.resilience.model.ImpactScale;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.IncidentRepository;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import roboguice.inject.ContextSingleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author georgepapas
 */
@ContextSingleton
public class ParseIncidentRepository implements IncidentRepository {

  @Override
  public void findById(RepositoryCommandResultListener<Incident> listener, String id) {
    ParseQuery query = new ParseQuery(Constants.TABLE_INCIDENT);

    Incident incident = null;
    try {
      ParseObject parseObject = query.get(id);
      incident = parseObjectToIncident(parseObject);
      incident.setId(id);
    } catch (ParseException e) {
      throw new RuntimeException("Failed to retrieve object with id " + id, e);
    }

    ArrayList<Incident> results = new ArrayList<Incident>();
    if (incident != null) {
      results.add(incident);
    }
    listener.commandComplete(new RepositoryCommandResult<Incident>(incident != null, results){});
  }

  @Override
  public void save(RepositoryCommandResultListener<Incident> listener, Incident incident) {
    ParseObject parseObject = new ParseObject(Constants.TABLE_INCIDENT);
    if (incident.getId() != null) {
      parseObject = retrieveParseObject(incident);
    }
    parseObject.put(Constants.COL_INCIDENT_NAME, incident.getName());
    parseObject.put(Constants.COL_INCIDENT_CATEGORY, incident.getCategory());
    parseObject.put(Constants.COL_INCIDENT_SUBCATEGORY, incident.getSubCategory());
    parseObject.put(Constants.COL_INCIDENT_IMPACT, incident.getImpact().name());
    parseObject.put(Constants.COL_INCIDENT_CREATION_DATE, incident.getDateCreated());
    parseObject.put(Constants.COL_INCIDENT_NOTE, incident.getNote());

    try {
      parseObject.save();

      // FIXME - mutating the object might be unexpected here.
      incident.setId(parseObject.getObjectId());
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }

    listener.commandComplete(new RepositoryCommandResult<Incident>(true, Arrays.asList(new Incident[]{incident})));
  }

  @Override
  public void findAll(RepositoryCommandResultListener<Incident> listener) {
    ParseQuery query = new ParseQuery(Constants.TABLE_INCIDENT);
    List<ParseObject> parseArray = null;
    try {
      parseArray = query.find();
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }


    listener.commandComplete(new RepositoryCommandResult<Incident>(true, toIncidentList(parseArray)));
  }

  @Override
  public void findClosest(RepositoryCommandResultListener<Incident> listener) {

  }

  @Override
  public void findTracked(RepositoryCommandResultListener<Incident> listener) {

  }

  private List<Incident> toIncidentList(List<ParseObject> parseArray) {

    List<Incident> incidents = new ArrayList<Incident>();
    for (ParseObject pObject : parseArray) {
      Incident incident = parseObjectToIncident(pObject);
      incidents.add(incident);
    }
    return incidents;

  }

  private ParseObject retrieveParseObject(Incident incident) {
    ParseObject parseObject = null;
    try {
      ParseQuery query = new ParseQuery(Constants.TABLE_INCIDENT);
      parseObject = query.get(incident.getId());
    }
    catch(ParseException e) {
      throw new RuntimeException(e);
    }

    return parseObject;
  }

  private Incident parseObjectToIncident(ParseObject pObject) {
    String id = pObject.getString(Constants.COL_ID);
    String name = pObject.getString(Constants.COL_INCIDENT_NAME);
    String category = pObject.getString(Constants.COL_INCIDENT_CATEGORY);
    String subCategory = pObject.getString(Constants.COL_INCIDENT_SUBCATEGORY);
    String impact = pObject.getString(Constants.COL_INCIDENT_IMPACT);
    long creationDate = pObject.getLong(Constants.COL_INCIDENT_CREATION_DATE);
    String note = pObject.getString(Constants.COL_INCIDENT_NOTE);

    ImpactScale impactScale = ImpactScale.valueOf(impact);
    Incident incident = new Incident(id, name, creationDate, note, category, subCategory, impactScale);

    return incident;
  }

}
