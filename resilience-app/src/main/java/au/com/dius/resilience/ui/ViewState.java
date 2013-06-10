package au.com.dius.resilience.ui;

import android.content.Intent;
import android.view.View;

public interface ViewState {
  void invoke(View container, Intent intent);
}