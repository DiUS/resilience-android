package au.com.dius.resilience.ui;

import android.content.Intent;
import android.view.View;
import au.com.dius.resilience.util.Logger;

import java.util.HashMap;
import java.util.Map;

public class ViewStateManager {

  private Map<String, ViewState> viewStates = new HashMap<String, ViewState>();

  private final View container;

  public ViewStateManager(View container) {
    this.container = container;
  }

  public void registerState(String id, ViewState viewState) {
    viewStates.put(id, viewState);
  }

  public void swap(String id, Intent intent) {
    ViewState viewState = viewStates.get(id);

    if (viewState == null) {
      Logger.e(this, "Couldn't find view state for ID: ", id);
      return;
    }

    viewState.invoke(container, intent);
  }
}
