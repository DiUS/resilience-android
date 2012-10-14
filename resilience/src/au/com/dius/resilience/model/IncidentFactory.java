package au.com.dius.resilience.model;

import javax.inject.Singleton;

@Singleton
public final class IncidentFactory {

  public static Incident createIncident(String name, Long dateCreated, String note, String category, String subCategory, ImpactScale scale) {
    Incident incident = new Incident(null, name, dateCreated, note, category, subCategory, scale);

    return incident;
  }
}
