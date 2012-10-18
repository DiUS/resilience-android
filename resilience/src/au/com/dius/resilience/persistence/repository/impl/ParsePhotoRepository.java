package au.com.dius.resilience.persistence.repository.impl;

import android.os.AsyncTask;
import android.util.Log;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.facade.CameraFacade;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.persistence.repository.PhotoRepository;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class ParsePhotoRepository implements PhotoRepository {

  public static final String LOG_TAG = ParsePhotoRepository.class.getName();
  
  private Incident incident;
  
  public ParsePhotoRepository() {
    
  }
  
  public ParsePhotoRepository(Incident incident) {
    this.incident = incident;
  }
  
  @Override
  public void save(final RepositoryCommandResultListener<Incident> listener,
      final Photo photo) {
    AsyncTask.execute(new Runnable() {
      @Override
      public void run() {
        byte[] bytes = CameraFacade.extractBytes(photo);
        final ParseFile parseFile = new ParseFile(Constants.PHOTO_FILENAME, bytes);
        try {
          saveIncidentWithPhoto(listener, parseFile);
        } catch (ParseException e) {
          Log.d(LOG_TAG, "Saving incident " + incident.getId() + " with photo failed.");
          listener.commandComplete(new RepositoryCommandResult<Incident>(
              false, incident));
        }
      }

      private void saveIncidentWithPhoto(
          final RepositoryCommandResultListener<Incident> listener,
          final ParseFile parseFile) throws ParseException {
        parseFile.save();
        final ParseObject parseObject = ParseObject.createWithoutData(Constants.TABLE_INCIDENT, incident.getId());
        if (parseObject.isDataAvailable()) {
          parseObject.fetchIfNeeded();
        }
        parseObject.put(Constants.COL_INCIDENT_PHOTO, parseFile);
        parseObject.saveEventually(new SaveCallback() {
          @Override
          public void done(ParseException ex) {
            Log.d(LOG_TAG, "Updated incident " + incident.getId() + " with photo " + (ex == null ? "succeeded." : "failed."));
            incident.setId(parseObject.getObjectId());
            listener.commandComplete(new RepositoryCommandResult<Incident>(
                ex == null, incident));
          }
        });
      }
    });

  }

  @Override
  public void findByIncident(RepositoryCommandResultListener<Incident> listener,
      long id) {
    throw new UnsupportedOperationException("Not implemented yet!");
  }
}
