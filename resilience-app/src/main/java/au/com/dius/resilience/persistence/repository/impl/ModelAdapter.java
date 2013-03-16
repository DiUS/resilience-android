package au.com.dius.resilience.persistence.repository.impl;

import java.util.List;

/**
 * @author georgepapas
 */
public interface ModelAdapter<D, P> {

  List<D> deserialise(List<P> persistable);

  /**
   * Builds a source object into a domain object
   * @param persistable the underlying persisted object
   * @return a domain
   */
  D deserialise(P persistable);

  /**
   * Builds an object that is able to be persisted from by the underlying repository
   * @param domainObject the domain object to transform
   * @return
   */
  P serialise(P persistable, D domainObject);
}
