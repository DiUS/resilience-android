package au.com.dius.resilience.ui.fragment.state;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.ui.ViewState;

public class LocationNotResolvedViewState implements ViewState {

  @Override
  public void invoke(View container, Intent intent) {
    View locatingIcon = container.findViewById(R.id.locating_icon);
    locatingIcon.setVisibility(View.VISIBLE);

    TextView locatingText = (TextView) container.findViewById(R.id.locating_text);
    locatingText.setText(locatingText.getContext().getString(R.string.locating_text));
  }
}
