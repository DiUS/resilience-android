package au.com.dius.resilience.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;
import au.com.dius.resilience.R;
import au.com.dius.resilience.facade.CameraFacade;
import au.com.dius.resilience.model.ImpactScale;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;
import au.com.dius.resilience.persistence.repository.IncidentRepository;
import au.com.dius.resilience.ui.Codes;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.Date;

public class EditIncidentActivity extends RoboActivity implements OnSeekBarChangeListener, RepositoryCommandResultListener<Incident> {

  @InjectView(R.id.category_spinner)
  private Spinner categorySpinner;

  @InjectView(R.id.sub_category_spinner)
  private Spinner subCategorySpinner;

  @InjectView(R.id.impact_scale)
  private SeekBar impactScale;

  @InjectView(R.id.notes)
  private EditText notes;
  
  // TODO - This object is shared between calls to another activity.
  // It may need to be bundled/deserialised during onPause/onResume?
  private CameraFacade cameraFacade;
  
  @Inject
  private IncidentRepository incidentRepository;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_incident);
    initialiseSpinners();
    
    impactScale.setOnSeekBarChangeListener(this);
    

    // FIXME - test this (-xxx-camera args not taking effect on my emulator)
    PackageManager pm = getPackageManager();
    boolean deviceHasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    Button cameraButton = (Button) findViewById(R.id.submit_photo);
    cameraButton.setEnabled(deviceHasCamera);
    cameraFacade = new CameraFacade(this);
  }

  private void initialiseSpinners() {
    ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
        R.array.categories, android.R.layout.simple_spinner_item);
    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    categorySpinner.setAdapter(categoryAdapter);
    
    ArrayAdapter<CharSequence> subCategoryadapter = ArrayAdapter.createFromResource(this,
        R.array.subcategories, android.R.layout.simple_spinner_item);
    subCategoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    subCategorySpinner.setAdapter(subCategoryadapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_edit_incident, menu);
    updateImpactLabel(ImpactScale.LOW);
    return true;
  }

  public void onSubmitClick(View button) {
    String incidentNote = notes.getText().toString();

    String category = categorySpinner.getSelectedItem().toString();
    String subCategory = subCategorySpinner.getSelectedItem().toString();
    ImpactScale impact = ImpactScale.fromCode(impactScale.getProgress());
    
    Incident incident = new Incident(
            category,
            Long.valueOf(new Date().getTime()),
            incidentNote,
            category,
            subCategory,
            impact);

    incident.addPhotos(cameraFacade.getPhotos());

    incidentRepository.save(this, incident);

    Log.d(getClass().getName(), "Saving incident: " + incident.toString());
  }

  public void onCameraClick(View button) {
    cameraFacade.captureImage();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    cameraFacade.processPhoto(requestCode, resultCode);
  }

  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    ImpactScale scale = ImpactScale.fromCode(progress);
    updateImpactLabel(scale);
  }

  private void updateImpactLabel(ImpactScale scale) {
    TextView impactDescription = (TextView) findViewById(R.id.impact_scale_desc);
    impactDescription.setText(scale.name());
  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {
    
  }

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {
    
  }

  @Override
  public void commandComplete(RepositoryCommandResult<Incident> result) {
    Toast.makeText(this, "Incident was " + (result.isSuccess() ? " saved" : " not saved"), Toast.LENGTH_SHORT).show();

    setResult(Codes.CreateIncident.RESULT_OK);
    finish();
  }
}
