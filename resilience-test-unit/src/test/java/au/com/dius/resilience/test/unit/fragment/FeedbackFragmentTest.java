package au.com.dius.resilience.test.unit.fragment;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import au.com.dius.resilience.ui.fragment.FeedbackFragment;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import junitx.util.PrivateAccessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(ResilienceTestRunner.class)
public class FeedbackFragmentTest {

  private FeedbackFragment feedbackFragment;

  @Mock
  private Button submitButton;
  private EditText feedbackText;

  @Before
  public void setUp() throws NoSuchFieldException {
    MockitoAnnotations.initMocks(this);

    feedbackFragment = new FeedbackFragment();

    feedbackText = new EditText(feedbackFragment.getActivity());
    PrivateAccessor.setField(feedbackFragment, "feedbackText", feedbackText);
  }

  @Test
  public void shouldSendFeedbackWhenTextBoxHasContent() {
    feedbackText.setText("Hello");
    feedbackFragment.onFeedbackSubmitClick(submitButton);
    assertNotNull(getStartedService());
  }

  @Test
  public void shouldNotSendFeedbackWhenTextIsEmpty() {
    feedbackFragment.onFeedbackSubmitClick(submitButton);
    assertNull(getStartedService());
  }

  private Intent getStartedService() {
    ShadowActivity activity = Robolectric.shadowOf(feedbackFragment.getActivity());
    return activity.getNextStartedService();
  }
}
