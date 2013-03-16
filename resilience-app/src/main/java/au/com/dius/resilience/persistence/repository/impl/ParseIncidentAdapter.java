package au.com.dius.resilience.persistence.repository.impl;

import au.com.dius.resilience.Constants;
import au.com.dius.resilience.model.Impact;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Point;
import au.com.dius.resilience.persistence.Columns;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import roboguice.inject.ContextSingleton;

import java.util.ArrayList;
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
            persistable.getString(Columns.Incident.NAME),
            dateCreated != null ? dateCreated.getTime() : 0,
            persistable.getString(Columns.Incident.NOTE),
            persistable.getString(Columns.Incident.CATEGORY),
            persistable.getString(Columns.Incident.SUBCATEGORY),
            Impact.valueOf(persistable.getString(Columns.Incident.IMPACT)));

    if (persistable.has(Columns.Incident.LOCATION)) {
      final ParseGeoPoint geoPoint = (ParseGeoPoint) persistable.get(Columns.Incident.LOCATION);
      incident.setPoint(new Point(geoPoint.getLatitude(), geoPoint.getLongitude()));
    }

    incident.setTrackers(persistable.<String>getList(Columns.Incident.TRACKED_BY));

    return incident;
  }

  @Override
  public ParseObject serialise(ParseObject parseObject, Incident incident) {

    parseObject.put(Columns.Incident.NAME, incident.getName());
    parseObject.put(Columns.Incident.CATEGORY, incident.getCategory());
    parseObject.put(Columns.Incident.SUBCATEGORY, incident.getSubCategory());
    parseObject.put(Columns.Incident.IMPACT, incident.getImpact().name());
    parseObject.put(Columns.Incident.NOTE, incident.getNote());

    Point point = incident.getPoint();
    if (point != null) {
      parseObject.put(
              Columns.Incident.LOCATION,
              new ParseGeoPoint(point.getLatitude(), point.getLongitude()));
    }

    return parseObject;
  }
}
