package au.com.dius.resilience.ui.fragment.state;

import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.widget.TextView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.intent.Extras;
import au.com.dius.resilience.ui.ViewState;

public class LocationResolvedViewState implements ViewState {

  @Override
  public void invoke(View container, Intent intent) {
    View locatingIcon = container.findViewById(R.id.locating_icon);
    locatingIcon.setVisibility(View.GONE);

    Location location = (Location) intent.getExtras().getParcelable(Extras.LOCATION);

    TextView locatingText = (TextView) container.findViewById(R.id.locating_text);

    if (location == null) {
      locatingText.setText(locatingText.getContext().getString(R.string.locate_failed));
    } else {
      locatingText.setText(locatingText.getContext().getString(R.string.located, "" + location.getLatitude() + ", "+ location.getLongitude()));
    }
  }
}