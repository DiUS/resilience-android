package au.com.dius.resilience.persistence;

import java.util.ArrayList;
import java.util.List;

import au.com.dius.resilience.Constants;
import au.com.dius.resilience.model.Incident;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class ParseIncidentRepository implements Repository<Incident> {

  @Override
  public boolean save(Incident incident) {
    ParseObject testObject = new ParseObject("Incident");
    testObject.put(Constants.COL_INCIDENT_NAME, incident.getName());
    testObject.put(Constants.COL_INCIDENT_CATEGORY, incident.getCategory());
    testObject.put(Constants.COL_INCIDENT_SUBCATEGORY, incident.getSubCategory());
    testObject.put(Constants.COL_INCIDENT_IMPACT, incident.getImpact().name());
    testObject.put(Constants.COL_INCIDENT_CREATION_DATE, incident.getDateCreated());
    testObject.put(Constants.COL_INCIDENT_NOTE, incident.getNote());
    
    testObject.saveEventually(new SaveCallback() {
      
      @Override
      public void done(ParseException arg0) {
      }
    });
    
    return true;
  }

  @Override
  public Incident findById(long id) {
    throw new RuntimeException("Not implemented.");
  }

  @Override
  public List<Incident> findAll() {
//    throw new RuntimeException("Not implemented.");
    
    return new ArrayList<Incident>();
  }
}
