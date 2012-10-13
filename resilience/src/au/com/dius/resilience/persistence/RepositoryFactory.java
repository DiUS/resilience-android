package au.com.dius.resilience.persistence;

import android.content.Context;
import au.com.dius.resilience.RuntimeProperties;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import com.google.inject.Singleton;

@Singleton
public class RepositoryFactory {

  public Repository<Incident> createIncidentRepository(Context context) {
    if (!RuntimeProperties.useLiveDb()) {
      return new SqlLiteRepository(context);
    }
    
    return new ParseRepository();
  }
  
  public Repository<Photo> createPhotoRepository(Context context) {
    return new PhotoRepository(context);
  }
}
