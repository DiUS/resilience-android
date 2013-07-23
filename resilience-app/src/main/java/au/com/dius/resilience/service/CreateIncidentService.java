package au.com.dius.resilience.service;

import android.app.IntentService;
import android.content.Intent;
import au.com.dius.resilience.intent.Extras;
import au.com.dius.resilience.persistence.repository.impl.CloudinaryRepository;
import au.com.dius.resilience.persistence.repository.impl.ServiceRequestRepository;
import au.com.dius.resilience.util.ImageCompressor;
import au.com.dius.resilience.util.Logger;
import au.com.justinb.open311.model.ServiceRequest;

import java.io.File;

public class CreateIncidentService extends IntentService {

  public static final String CREATE_INCIDENT_SERVICE = "CreateIncidentService";
  public static final int PHOTO_QUALITY = 30;

  private ServiceRequestRepository serviceRequestRepository;

  public CreateIncidentService() {
    super(CREATE_INCIDENT_SERVICE);

    serviceRequestRepository = new ServiceRequestRepository();
    serviceRequestRepository.subscribe(this);
  }

  @Override
  public void onDestroy() {
    serviceRequestRepository.unsubscribe(this);
    super.onDestroy();
  }

  @Override
  protected void onHandleIntent(Intent intent) {

    String photoUri = intent.getStringExtra(Extras.PHOTO_LOCAL_URI);
    ServiceRequest.Builder serviceRequestBuilder =
      (ServiceRequest.Builder) intent.getSerializableExtra(Extras.SERVICE_REQUEST_BUILDER);

    try {

      Logger.d(this, "Compressing and uploading photo: ", photoUri);

      File compressedFile = new ImageCompressor().compress(photoUri, PHOTO_QUALITY);
      String url = new CloudinaryRepository(getResources()).create(compressedFile);
      Logger.d(this, "Uploaded photo to cloudinary with url: ", url);

      Logger.d(this, "Submitting service request..");
      serviceRequestBuilder.mediaUrl(url);
      serviceRequestRepository.create(serviceRequestBuilder.createServiceRequest());

    } catch (Exception e) {
//      Toast.makeText(finalThis, "Photo upload failed.", Toast.LENGTH_LONG).show();
    }
  }
}
