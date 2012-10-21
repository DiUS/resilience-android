package au.com.dius.resilience.bootstrap;

import au.com.dius.resilience.RuntimeProperties;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.IncidentRepository;
import au.com.dius.resilience.persistence.repository.PhotoRepository;
import au.com.dius.resilience.persistence.repository.impl.ModelAdapter;
import au.com.dius.resilience.persistence.repository.impl.ParseIncidentAdapter;
import au.com.dius.resilience.persistence.repository.impl.ParseIncidentRepository;
import au.com.dius.resilience.persistence.repository.impl.ParsePhotoRepository;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.parse.ParseObject;
import roboguice.inject.ContextSingleton;

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
      bind(PhotoRepository.class).to(ParsePhotoRepository.class).in(ContextSingleton.class);
      bind(new TypeLiteral<ModelAdapter< Incident, ParseObject>>(){}).to(ParseIncidentAdapter.class).in(ContextSingleton.class);
    }
}
