package au.com.dius.resilience.ui.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.persistence.repository.Repository;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

public class ImageBalloonOverlayView<Item extends OverlayItem> extends BalloonOverlayView<OverlayItem> {

  public static final String PHOTO_LOADED = "photo_loaded";
  public static final String PHOTO_BITMAP = "photo_bitmap";
  private TextView title;
  private TextView snippet;
  private ImageView image;

  private Repository repository;
  private ProgressBar progressBar;

  private Incident tappedIncident;

  BroadcastReceiver photoLoaderBroadcastReceiver;

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

    photoLoaderBroadcastReceiver = new PhotoLoaderBroadcastReceiver();
    context.registerReceiver(photoLoaderBroadcastReceiver, new IntentFilter(PHOTO_LOADED));
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

    // FIXME. We go from UI thread -> Async thread -> broadcast -> UI thread
    // in this class again. Seems way too complex.
    AsyncTask.execute(new Runnable() {
      @Override
      public void run() {
        Photo photo = repository.findPhotoByIncident(tappedIncident.getId());

        Intent intent = new Intent(PHOTO_LOADED);
        intent.putExtra(PHOTO_BITMAP, photo.getBitmap());

        getContext().sendBroadcast(intent);
      }
    });
  }

  private final class PhotoLoaderBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      progressBar.setVisibility(View.GONE);
      Bitmap bitmap = (Bitmap) intent.getExtras().get(PHOTO_BITMAP);

      if (bitmap != null) {
        image.setVisibility(View.VISIBLE);
        image.setImageBitmap(bitmap);
      }
    }
  }
}
