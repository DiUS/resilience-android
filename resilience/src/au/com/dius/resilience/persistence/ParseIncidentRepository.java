package au.com.dius.resilience.persistence;

import java.util.ArrayList;
import java.util.List;

import au.com.dius.resilience.Constants;
import au.com.dius.resilience.model.ImpactScale;
import android.util.Log;
import au.com.dius.resilience.model.Incident;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ParseIncidentRepository implements Repository<Incident> {

  @Override
  public boolean save(Incident incident) {
    ParseObject testObject = new ParseObject(Constants.TABLE_INCIDENT);
    testObject.put(Constants.COL_INCIDENT_NAME, incident.getName());
    testObject.put(Constants.COL_INCIDENT_CATEGORY, incident.getCategory());
    testObject.put(Constants.COL_INCIDENT_SUBCATEGORY, incident.getSubCategory());
    testObject.put(Constants.COL_INCIDENT_IMPACT, incident.getImpact().name());
    testObject.put(Constants.COL_INCIDENT_CREATION_DATE, incident.getDateCreated());
    testObject.put(Constants.COL_INCIDENT_NOTE, incident.getNote());

    try {
      testObject.save();
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }

    return true;
  }

  @Override
  public Incident findById(long id) {
    Log.d("INCIDENT REPO", "findById");
    return null;
//    throw new RuntimeException("Not implemented.");
  }

  @Override
  public List<Incident> findAll() {
    ParseQuery query = new ParseQuery(Constants.TABLE_INCIDENT);
    List<ParseObject> parseArray = null;
    try {
      parseArray = query.find();
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }

    return toIncidentList(parseArray);
  }

  private List<Incident> toIncidentList(List<ParseObject> parseArray) {
    
    List<Incident> incidents = new ArrayList<Incident>();
    for (ParseObject pObject : parseArray) {
      String id = pObject.getString(Constants.COL_ID);
      String name = pObject.getString(Constants.COL_INCIDENT_NAME);
      String category = pObject.getString(Constants.COL_INCIDENT_CATEGORY);
      String subCategory = pObject.getString(Constants.COL_INCIDENT_SUBCATEGORY);
      String impact = pObject.getString(Constants.COL_INCIDENT_IMPACT);
      long creationDate = pObject.getLong(Constants.COL_INCIDENT_CREATION_DATE);
      String note = pObject.getString(Constants.COL_INCIDENT_NOTE);
      
      ImpactScale impactScale = ImpactScale.valueOf(impact);
      Incident incident = new Incident(id, name, creationDate, note, category, subCategory, impactScale);
      incidents.add(incident);
    }
    return incidents;

  }
}
