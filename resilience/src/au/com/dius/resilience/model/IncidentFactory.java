package au.com.dius.resilience.model;

public final class IncidentFactory {

  public static final Incident createIncident(String name, Long dateCreated, String note) {
    Incident incident = new Incident(null, name, dateCreated, note);

    return incident;
  }
}
