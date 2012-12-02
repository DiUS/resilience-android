package au.com.dius.resilience.persistence.repository.impl;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.facade.CameraFacade;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.model.Point;
import au.com.dius.resilience.persistence.repository.Repository;
import com.google.android.maps.GeoPoint;
import com.google.inject.Inject;
import com.parse.*;
import roboguice.inject.ContextSingleton;

import java.util.ArrayList;
import java.util.List;

@ContextSingleton
public class ParseRepository implements Repository {

  private static final String TAG = ParseRepository.class.getName();

  final ParseIncidentAdapter incidentAdapter;

  @Inject
  public ParseRepository(ParseIncidentAdapter incidentAdapter) {
    this.incidentAdapter = incidentAdapter;
  }

  @Override
  public List<Incident> findIncidentsWithinDistanceKM(Point point, int distance) {
    ParseGeoPoint parseGeoPoint = new ParseGeoPoint(point.getLatitude(), point.getLongitude());

    ParseQuery parseQuery = new ParseQuery(Constants.TABLE_INCIDENT);
    parseQuery.whereWithinKilometers(Constants.COL_INCIDENT_LOCATION, parseGeoPoint, distance);

    List<ParseObject> incidents = loadIncidents(parseQuery);
    return incidentAdapter.deserialise(incidents);
  }

  @Override
  public List<Incident> findIncidentsWithinBoundingBox(Point southWest, Point northEast) {

    ParseGeoPoint southWestGeoPoint = new ParseGeoPoint(southWest.getLatitude(), southWest.getLongitude());
    ParseGeoPoint northEastGeoPoint = new ParseGeoPoint(northEast.getLatitude(), northEast.getLongitude());

    ParseQuery parseQuery = new ParseQuery(Constants.TABLE_INCIDENT);
    parseQuery.whereWithinGeoBox(Constants.COL_INCIDENT_LOCATION, southWestGeoPoint, northEastGeoPoint);

    List<ParseObject> parseObjects = loadIncidents(parseQuery);

    return incidentAdapter.deserialise(parseObjects);
  }

  @Override
  public List<Incident> findIncidents() {
    ParseQuery query = new ParseQuery(Constants.TABLE_INCIDENT);
    query.orderByDescending(Constants.COL_INCIDENT_CREATION_DATE);

    List<ParseObject> parseObjects = loadIncidents(query);
    return incidentAdapter.deserialise(parseObjects);
  }

  @Override
  public Photo findPhotoByIncident(final String incidentId) {
    ParseQuery parseQuery = new ParseQuery(Constants.TABLE_INCIDENT);

    Photo photo = null;
    try {
      ParseObject incident = parseQuery.get(incidentId);
      final ParseFile parseFile = (ParseFile) incident.get(Constants.COL_INCIDENT_PHOTO);
      if (parseFile != null) {
        Log.d(TAG, "Found photo for incident " + incidentId + ", retrieving data..");

        byte[] data = parseFile.getData();
        Log.d(TAG, "Retrieved " + (data == null ? 0 : data.length) + " bytes of data.");

        Bitmap bitmap = CameraFacade.decodeBytes(data);
        photo = new Photo(Uri.parse(parseFile.getUrl()), bitmap);
      }
    } catch (ParseException e) {
      Log.d(TAG, "Failed to load photo: ", e);
      return null;
    }
    return photo;
  }

  private List<ParseObject> loadIncidents(ParseQuery query) {
    try {
      return query.find();
    } catch (ParseException e) {
      Log.d(TAG, "Loading all incidents failed with: ", e);
      return emptyList();
    }
  }

  private ArrayList<ParseObject> emptyList() {
    return new ArrayList<ParseObject>();
  }
}
