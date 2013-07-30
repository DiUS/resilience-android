package au.com.dius.resilience.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import au.com.dius.resilience.R;
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
  private static final int DONE = 100;

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

    ProgressNotifier progressNotifier = new ProgressNotifier(this,
      getString(R.string.reporting, serviceRequest.getServiceName()));

    try {

      progressNotifier.setText(getString(R.string.uploading_photo));

      File compressedFile = new ImageCompressor().compress(photoUri, PHOTO_QUALITY);
      String url = new CloudinaryRepository(getResources()).create(compressedFile);

      progressNotifier.setText(getString(R.string.uploading_issue));
      serviceRequestBuilder.mediaUrl(url);

      requestAdapter.create(serviceRequestBuilder.createServiceRequest());

      progressNotifier.setText(getString(R.string.upload_complete));
      progressNotifier.setProgress(DONE);

    } catch (Throwable e) {
      progressNotifier.setFailureAction(PendingIntent.getService(this, 0, intent, 0));
      progressNotifier.setText(getString(R.string.upload_failed));
      progressNotifier.setProgress(DONE);
      Logger.e(this, "Error while uploading incident: ", e, e.getCause().getMessage());
    }
  }
}
