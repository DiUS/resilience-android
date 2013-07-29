package au.com.dius.resilience.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import au.com.dius.resilience.intent.Extras;
import au.com.dius.resilience.persistence.repository.impl.CloudinaryRepository;
import au.com.dius.resilience.util.ImageCompressor;
import au.com.dius.resilience.util.Logger;
import au.com.justinb.open311.GenericRequestAdapter;
import au.com.justinb.open311.model.ServiceRequest;

import java.io.File;

public class CreateIncidentService extends IntentService {

  private static final String CREATE_INCIDENT_SERVICE = "CreateIncidentService";
  private static final int PHOTO_QUALITY = 30;

  private GenericRequestAdapter<ServiceRequest> requestAdapter;

  public CreateIncidentService() {
    super(CREATE_INCIDENT_SERVICE);
    requestAdapter = new GenericRequestAdapter<ServiceRequest>(ServiceRequest.class);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  protected void onHandleIntent(Intent intent) {

    String photoUri = intent.getStringExtra(Extras.PHOTO_LOCAL_URI);
    ServiceRequest.Builder serviceRequestBuilder =
      (ServiceRequest.Builder) intent.getSerializableExtra(Extras.SERVICE_REQUEST_BUILDER);

    ServiceRequest serviceRequest = serviceRequestBuilder.createServiceRequest();

    ProgressNotifier progressNotifier = new ProgressNotifier(this, "Reporting " + serviceRequest.getServiceName());

    try {

      progressNotifier.setText("Compressing and uploading photo..");
      Logger.d(this, "Compressing and uploading photo: ", photoUri);

      File compressedFile = new ImageCompressor().compress(photoUri, PHOTO_QUALITY);
      String url = new CloudinaryRepository(getResources()).create(compressedFile);
      Logger.d(this, "Uploaded photo to cloudinary with url: ", url);

      progressNotifier.setText("Creating issue..");

      Logger.d(this, "Submitting service request..");
      serviceRequestBuilder.mediaUrl(url);

      requestAdapter.create(serviceRequestBuilder.createServiceRequest());

      progressNotifier.setText("Complete.");
      progressNotifier.setProgress(100);

    } catch (Throwable e) {
      progressNotifier.setFailureAction(PendingIntent.getService(this, 0, intent, 0));
      progressNotifier.setText("Upload failed. Please try again in a few minutes.");
      progressNotifier.setProgress(100);
      Logger.e(this, "Error while uploading incident: ", e, e.getCause().getMessage());
    }
  }
}
