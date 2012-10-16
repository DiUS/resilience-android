package au.com.dius.resilience.bootstrap;

import roboguice.inject.ContextSingleton;
import au.com.dius.resilience.RuntimeProperties;
import au.com.dius.resilience.persistence.repository.IncidentRepository;
import au.com.dius.resilience.persistence.repository.PhotoRepository;
import au.com.dius.resilience.persistence.repository.impl.ParseIncidentRepository;
import au.com.dius.resilience.persistence.repository.impl.SqlLitePhotoRepository;

import com.google.inject.AbstractModule;

/**
 * @author georgepapas
 */
public class ResilienceModule extends AbstractModule {

  @Override
  protected void configure() {

    if (RuntimeProperties.useLiveDb()) {
    }
    else
      bind(IncidentRepository.class).to(ParseIncidentRepository.class).in(ContextSingleton.class);
      bind(PhotoRepository.class).to(SqlLitePhotoRepository.class).in(ContextSingleton.class);
    }
}
