package au.com.dius.resilience.persistence.repository.impl;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.facade.CameraFacade;
import au.com.dius.resilience.model.Feedback;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.model.Point;
import au.com.dius.resilience.persistence.Columns;
import au.com.dius.resilience.persistence.repository.Repository;
import com.google.inject.Inject;
import com.parse.*;
import roboguice.inject.ContextSingleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static au.com.dius.resilience.persistence.Columns.Incident.TABLE_NAME;

@ContextSingleton
@Deprecated
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

    ParseQuery parseQuery = new ParseQuery(Columns.Incident.TABLE_NAME);
    parseQuery.whereWithinKilometers(Columns.Incident.LOCATION, parseGeoPoint, distance);

    List<ParseObject> incidents = loadIncidents(parseQuery);
    return incidentAdapter.deserialise(incidents);
  }

  @Override
  public List<Incident> findIncidentsWithinBoundingBox(Point southWest, Point northEast) {

    ParseGeoPoint southWestGeoPoint = new ParseGeoPoint(southWest.getLatitude(), southWest.getLongitude());
    ParseGeoPoint northEastGeoPoint = new ParseGeoPoint(northEast.getLatitude(), northEast.getLongitude());

    ParseQuery parseQuery = new ParseQuery(Columns.Incident.TABLE_NAME);
    parseQuery.whereWithinGeoBox(Columns.Incident.LOCATION, southWestGeoPoint, northEastGeoPoint);

    List<ParseObject> parseObjects = loadIncidents(parseQuery);

    return incidentAdapter.deserialise(parseObjects);
  }

  @Override
  public List<Incident> findIncidents() {
    ParseQuery query = new ParseQuery(Columns.Incident.TABLE_NAME);
    query.orderByDescending(Columns.Incident.CREATION_DATE);

    List<ParseObject> parseObjects = loadIncidents(query);
    return incidentAdapter.deserialise(parseObjects);
  }

  @Override
  public Photo findPhotoByIncident(final String incidentId) {
    ParseQuery parseQuery = new ParseQuery(Columns.Incident.TABLE_NAME);

    Photo photo = null;
    try {
      ParseObject incident = parseQuery.get(incidentId);
      final ParseFile parseFile = (ParseFile) incident.get(Columns.Incident.PHOTO);
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

  @Override
  public boolean createIncident(Incident incident) {
    final ParseObject parseObject = ParseObject.createWithoutData(Columns.Incident.TABLE_NAME, incident.getId());

    incidentAdapter.serialise(parseObject, incident);
    try {
      parseObject.save();
      incident.setId(parseObject.getObjectId());

      if (incident.hasPhotos()) {
        savePhoto(incident, incident.getPhotos().get(0));
      }
    } catch (ParseException e) {
      return false;
    }
    return true;
  }

  @Override
  public boolean trackIncident(Incident incident, String userIdentifier) {
    final ParseObject parseObject = ParseObject.createWithoutData(Columns.Incident.TABLE_NAME, incident.getId());
    incidentAdapter.serialise(parseObject, incident);

    parseObject.addUnique(Columns.Incident.TRACKED_BY, userIdentifier);
    try {
      parseObject.save();
    } catch (ParseException e) {
      Log.i(TAG, "Could not update tracking information");
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public boolean untrackIncident(Incident incident, String userIdentifier) {
    final ParseObject parseObject = ParseObject.createWithoutData(Columns.Incident.TABLE_NAME, incident.getId());
    incidentAdapter.serialise(parseObject, incident);

    Collection<String> ids = new ArrayList<String>();
    ids.add(userIdentifier);
    parseObject.removeAll(Columns.Incident.TRACKED_BY, ids);

    try {
      parseObject.save();
    } catch (ParseException e) {
      Log.i(TAG, "Could not update tracking information");
      e.printStackTrace();
      return false;
    }
    return true;  }

  @Override
  public boolean sendFeedback(Feedback feedback) {
    ParseObject parseObject = new ParseObject(Columns.Feedback.TABLE_NAME);
    parseObject.put(Columns.Feedback.TEXT, feedback.getText());
    parseObject.put(Columns.Feedback.PHONE_ID, feedback.getDeviceId());

    try {
      parseObject.save();
    } catch (ParseException e) {
      e.printStackTrace();
      Log.d(TAG, "Could not send feedback");
      return false;
    }

    return true;
  }

  private void savePhoto(Incident incident, Photo photo) throws ParseException {
    byte[] bytes = CameraFacade.extractBytes(photo);
    final ParseFile parseFile = new ParseFile(Constants.PHOTO_FILENAME, bytes);

    parseFile.save();

    final ParseObject parseObject = ParseObject.createWithoutData(Columns.Incident.TABLE_NAME, incident.getId());
    if (parseObject.isDataAvailable()) {
      parseObject.fetchIfNeeded();
    }
    parseObject.put(Columns.Incident.PHOTO, parseFile);
    parseObject.saveEventually(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        Log.d(TAG, "Saved incident with photo data");
      }
    });
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
