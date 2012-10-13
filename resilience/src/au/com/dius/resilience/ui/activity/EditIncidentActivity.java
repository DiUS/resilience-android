package au.com.dius.resilience.ui.activity;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.facade.CameraFacade;
import au.com.dius.resilience.model.ImpactScale;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.IncidentFactory;
import au.com.dius.resilience.persistence.Repository;
import au.com.dius.resilience.persistence.RepositoryFactory;

public class EditIncidentActivity extends Activity implements OnSeekBarChangeListener {

  private Spinner categorySpinner;
  private Spinner subCategorySpinner;
  private SeekBar impactScale;
  private EditText notes;
  
  // TODO - This object is shared between calls to another activity.
  // It may need to be bundled/deserialised during onPause/onResume?
  private CameraFacade cameraFacade;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_incident);

    categorySpinner = (Spinner) findViewById(R.id.category_spinner);
    subCategorySpinner = (Spinner) findViewById(R.id.sub_category_spinner);
    initialiseSpinners();
    
    impactScale = (SeekBar) findViewById(R.id.impact_scale);
    impactScale.setOnSeekBarChangeListener(this);
    
    notes = (EditText) findViewById(R.id.notes);
    
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
    
    Repository<Incident> repository = RepositoryFactory.createIncidentRepository(this);
    Incident incident = IncidentFactory.createIncident(category, Long.valueOf(new Date().getTime()), incidentNote, category, subCategory, impact);

    incident.addPhotos(cameraFacade.getPhotos());
    
    Log.d(getClass().getName(), "Saving incident: " + incident.toString());

    repository.save(incident);
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
}
