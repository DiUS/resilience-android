package au.com.dius.resilience.persistence;

import android.content.Context;
import au.com.dius.resilience.RuntimeProperties;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;

public class RepositoryFactory {

  public static Repository<Incident> createIncidentRepository(Context context) {
    if (!RuntimeProperties.useLiveDb()) {
      return new SqlLiteRepository(context);
    }
    
    return new ParseRepository();
  }
  
  public static Repository<Photo> createPhotoRepository() {
    return new PhotoRepository();
  }
}
