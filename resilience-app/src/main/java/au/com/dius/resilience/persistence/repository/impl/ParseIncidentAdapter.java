package au.com.dius.resilience.persistence.repository.impl;

import au.com.dius.resilience.Constants;
import au.com.dius.resilience.model.Impact;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Point;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import roboguice.inject.ContextSingleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@ContextSingleton
public class ParseIncidentAdapter implements ModelAdapter<Incident, ParseObject> {

  @Override
  public List<Incident> deserialise(List<ParseObject> persistable) {
    List<Incident> incidents = new ArrayList<Incident>();
    for (ParseObject parseObject : persistable) {
      incidents.add(deserialise(parseObject));
    }
    return incidents;
  }

  @Override
  public Incident deserialise(ParseObject persistable) {
    final Date dateCreated = persistable.getCreatedAt();
    Incident incident = new Incident(
            persistable.getObjectId(),
            persistable.getString(Constants.COL_INCIDENT_NAME),
            dateCreated != null ? dateCreated.getTime() : 0,
            persistable.getString(Constants.COL_INCIDENT_NOTE),
            persistable.getString(Constants.COL_INCIDENT_CATEGORY),
            persistable.getString(Constants.COL_INCIDENT_SUBCATEGORY),
            Impact.valueOf(persistable.getString(Constants.COL_INCIDENT_IMPACT)));

    if (persistable.has(Constants.COL_INCIDENT_LOCATION)) {
      final ParseGeoPoint geoPoint = (ParseGeoPoint) persistable.get(Constants.COL_INCIDENT_LOCATION);
      incident.setPoint(new Point(geoPoint.getLatitude(), geoPoint.getLongitude()));
    }

    incident.setTrackers(persistable.<String>getList(Constants.COL_TRACKED_BY));

    return incident;
  }

  @Override
  public ParseObject serialise(ParseObject parseObject, Incident incident) {

    parseObject.put(Constants.COL_INCIDENT_NAME, incident.getName());
    parseObject.put(Constants.COL_INCIDENT_CATEGORY, incident.getCategory());
    parseObject.put(Constants.COL_INCIDENT_SUBCATEGORY, incident.getSubCategory());
    parseObject.put(Constants.COL_INCIDENT_IMPACT, incident.getImpact().name());
    parseObject.put(Constants.COL_INCIDENT_NOTE, incident.getNote());

    Point point = incident.getPoint();
    if (point != null) {
      parseObject.put(
              Constants.COL_INCIDENT_LOCATION,
              new ParseGeoPoint(point.getLatitude(), point.getLongitude()));
    }

    return parseObject;
  }
}
