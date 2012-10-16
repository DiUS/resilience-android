package au.com.dius.resilience.bootstrap;

import au.com.dius.resilience.persistence.repository.IncidentRepository;
import au.com.dius.resilience.persistence.repository.PhotoRepository;
import au.com.dius.resilience.persistence.repository.SqlLiteIncidentRepository;
import au.com.dius.resilience.persistence.repository.SqlLitePhotoRepository;
import com.google.inject.AbstractModule;
import roboguice.inject.ContextSingleton;

/**
 * @author georgepapas
 */
public class ResilienceModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(IncidentRepository.class).to(SqlLiteIncidentRepository.class).in(ContextSingleton.class);
    bind(PhotoRepository.class).to(SqlLitePhotoRepository.class).in(ContextSingleton.class);

  }

}
