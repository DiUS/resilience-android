package au.com.dius.resilience.persistence.repository.impl;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.facade.CameraFacade;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.persistence.repository.PhotoRepository;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

@Deprecated
/**
 * To be moved into {@link ParseRepository}
 */
public class ParsePhotoRepository implements PhotoRepository {

  public static final String LOG_TAG = ParsePhotoRepository.class.getName();

  @Override
  public void save(final RepositoryCommandResultListener<Incident> listener,
                   final Photo photo, final Incident incident) {
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
  @Deprecated
  /**
   * To be moved into the parse repository and accessed by the photo loader
   */
  public void findByIncident(final RepositoryCommandResultListener<Photo> listener,
                             final Incident incident) {

    if (incident.getId() == null) {
      throw new RuntimeException("Incident has not yet been persisted!");
    }

    ParseQuery parseQuery = new ParseQuery(Constants.TABLE_INCIDENT);
    parseQuery.getInBackground(incident.getId(), new GetCallback() {
      @Override
      public void done(ParseObject parseIncident, ParseException ex) {
        final ParseFile parseFile = (ParseFile) parseIncident.get(Constants.COL_INCIDENT_PHOTO);
        if (parseFile == null) {
          listener.commandComplete(new RepositoryCommandResult<Photo>(false, new ArrayList<Photo>()));
          return;
        }

        Log.d(LOG_TAG, "Found photo for incident " + incident.getId() + ", retriving data..");
        parseFile.getDataInBackground(new GetDataCallback() {
          @Override
          public void done(byte[] data, ParseException ex) {
            Log.d(LOG_TAG, "Retrieved " + (data == null ? 0 : data.length) + " bytes of data.");
            Bitmap bitmap = CameraFacade.decodeBytes(data);
            Photo photo = new Photo(Uri.parse(parseFile.getUrl()), bitmap);
            listener.commandComplete(new RepositoryCommandResult<Photo>(ex == null, photo));
          }
        });
      }
    });
  }
}
