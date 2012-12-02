package au.com.dius.resilience.ui.map;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.persistence.repository.Repository;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

public class ImageBalloonOverlayView<Item extends OverlayItem> extends BalloonOverlayView<OverlayItem> {

  private TextView title;
  private TextView snippet;
  private ImageView image;

  private Repository repository;
  private ProgressBar progressBar;

  private Incident tappedIncident;

  /**
   * Create a new BalloonOverlayView.
   *
   * @param context             - The activity context.
   * @param balloonBottomOffset - The bottom padding (in pixels) to be applied
   * @param tappedIncident
   * @param repository
   */
  public ImageBalloonOverlayView(Context context, int balloonBottomOffset, Incident tappedIncident, Repository repository) {
    super(context, balloonBottomOffset);
    this.tappedIncident = tappedIncident;
    this.repository = repository;
  }

  @Override
  protected void setupView(Context context, final ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
      .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View v = inflater.inflate(R.layout.image_balloon_overlay, parent);
    title = (TextView) v.findViewById(R.id.balloon_item_title);
    snippet = (TextView) v.findViewById(R.id.balloon_item_snippet);
    image = (ImageView) v.findViewById(R.id.balloon_item_image);
    progressBar = (ProgressBar) v.findViewById(R.id.image_load_progress_bar);
  }


  @Override
  protected void setBalloonData(OverlayItem item, ViewGroup parent) {
    title.setText(item.getTitle());
    snippet.setText(item.getSnippet());

    image.setVisibility(View.INVISIBLE);
    progressBar.setVisibility(View.VISIBLE);

    PhotoLoadTask loadTask = new PhotoLoadTask();
    loadTask.execute();
  }

  public void setTappedIncident(Incident tappedIncident) {
    this.tappedIncident = tappedIncident;
  }

  private final class PhotoLoadTask extends AsyncTask<Object, Object, Photo> {

    @Override
    protected Photo doInBackground(Object... params) {
      return repository.findPhotoByIncident(tappedIncident.getId());
    }

    @Override
    protected void onPostExecute(Photo photo) {
      progressBar.setVisibility(View.INVISIBLE);
      if (photo != null) {
        image.setVisibility(View.VISIBLE);
        image.setImageBitmap(photo.getBitmap());
      }
    }
  }
}
