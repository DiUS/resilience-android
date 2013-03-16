package au.com.dius.resilience.persistence.repository.impl;

import android.os.AsyncTask;
import android.util.Log;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.Columns;
import au.com.dius.resilience.persistence.repository.IncidentRepository;
import au.com.dius.resilience.persistence.repository.PhotoRepository;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;
import com.google.inject.Inject;
import com.parse.*;
import roboguice.inject.ContextSingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * @author georgepapas
 */
@ContextSingleton
@Deprecated
public class ParseIncidentRepository implements IncidentRepository {

  public static final String LOG_TAG = ParseIncidentRepository.class.getName();

  @Inject
  PhotoRepository photoRepository;

  @Inject
  private ModelAdapter<Incident, ParseObject> parseIncidentAdapter;

  @Override
  public void findById(
      final RepositoryCommandResultListener<Incident> listener, final String id) {
    final ParseQuery query = new ParseQuery(Columns.Incident.TABLE_NAME);
    query.getInBackground(id, new GetCallback() {
      @Override
      public void done(ParseObject parseObject, ParseException ex) {
        Incident incident = parseIncidentAdapter.deserialise(parseObject);
        incident.setId(id);
        listener.commandComplete(new RepositoryCommandResult<Incident>(
            ex == null, incident));
      }
    });
  }

  @Override
  public void save(final RepositoryCommandResultListener<Incident> listener, final Incident incident) {
    AsyncTask.execute(new Runnable() {
      @Override
      public void run() {
        final ParseObject parseObject = ParseObject.createWithoutData(Columns.Incident.TABLE_NAME, incident.getId());
        try {
          // Update object if it has an ID, otherwise we'll use the one we just created.
          if(parseObject.isDataAvailable()) {
            parseObject.fetchIfNeeded();
          }
        } catch (ParseException e) {
          listener.commandComplete(new RepositoryCommandResult<Incident>(
              false, incident));
          return;
        }
        
        parseIncidentAdapter.serialise(parseObject, incident);

        Log.d(LOG_TAG, "Saving incident in async task, thread is " + Thread.currentThread().getName());

        parseObject.saveEventually(new SaveCallback() {
          @Override
          public void done(ParseException ex) {
            if (incident.getId() == null) {
              Log.d(LOG_TAG, "Saved new incident " + parseObject.getObjectId() + (ex == null ? "succeeded." : "failed."));
            }
            else {
              Log.d(LOG_TAG, "Updated incident " + incident.getId() + (ex == null ? "succeeded." : "failed."));
            }
            incident.setId(parseObject.getObjectId());
            
            if (incident.getPhotos().size() > 0) {
              photoRepository.save(listener, incident.getPhotos().get(0), incident);
            }
            else {
              listener.commandComplete(new RepositoryCommandResult<Incident>(
                  ex == null, incident));
            }
          }
        });
      }
    });
  }


  @Override
  public void findAll(final RepositoryCommandResultListener<Incident> listener) {
    ParseQuery query = new ParseQuery(Columns.Incident.TABLE_NAME);
    query.orderByDescending(Columns.Incident.CREATION_DATE);
    query.findInBackground(new FindCallback() {
      @Override
      public void done(List<ParseObject> results, ParseException ex) {
        List<Incident> incidentList = new ArrayList<Incident>();
        if (ex == null) {
          incidentList.addAll(toIncidentList(results));
        }
        listener.commandComplete(new RepositoryCommandResult<Incident>(
            ex == null, incidentList));
      }
    });
  }

  @Override
  public void findClosest(RepositoryCommandResultListener<Incident> listener) {

  }

  @Override
  public void findTracked(RepositoryCommandResultListener<Incident> listener) {

  }

  private List<Incident> toIncidentList(List<ParseObject> parseArray) {

    List<Incident> incidents = new ArrayList<Incident>();
    for (ParseObject pObject : parseArray) {
      incidents.add(parseIncidentAdapter.deserialise(pObject));
    }
    return incidents;

  }

}
