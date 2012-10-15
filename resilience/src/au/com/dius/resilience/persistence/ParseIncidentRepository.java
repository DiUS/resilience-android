package au.com.dius.resilience.persistence;

import android.util.Log;
import au.com.dius.resilience.model.Incident;

import java.util.List;

public class ParseIncidentRepository implements Repository<Incident> {

  @Override
  public boolean save(Incident incident) {
    Log.d("INCIDENT REPO", "save called");
    return true;
    //throw new RuntimeException("Not implemented.");
  }

  @Override
  public Incident findById(long id) {
    Log.d("INCIDENT REPO", "findById");
    return null;
//    throw new RuntimeException("Not implemented.");
  }

  @Override
  public List<Incident> findAll() {
    Log.d("INCIDENT REPO", "findAll");
    throw new RuntimeException("Not implemented.");
  }
}
