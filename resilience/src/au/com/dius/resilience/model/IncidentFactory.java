package au.com.dius.resilience.model;

public final class IncidentFactory {

  public static final Incident createIncident(String name, Long dateCreated, String note, String category, String subCategory, ImpactScale scale) {
    Incident incident = new Incident(null, name, dateCreated, note, category, subCategory, scale);

    return incident;
  }
}
