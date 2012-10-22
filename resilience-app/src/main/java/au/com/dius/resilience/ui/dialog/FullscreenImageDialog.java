package au.com.dius.resilience.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import au.com.dius.resilience.R;

public class FullscreenImageDialog extends DialogFragment {

  private ImageView imageView;

  public FullscreenImageDialog(ImageView imageView) {
    this.imageView = imageView;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    LinearLayout view = (LinearLayout) inflater.inflate(R.layout.image_popup_layout, container, false);
    ImageView photo = (ImageView) view.findViewById(R.id.fullscreen_photo);
    photo.setImageBitmap(((BitmapDrawable)imageView.getDrawable()).getBitmap());

    return inflater.inflate(R.layout.image_popup_layout, container, false);
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    Dialog dialog = super.onCreateDialog(savedInstanceState);
//    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//   dialog.getWindow().setFlags(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);

    return dialog;
  }
}
