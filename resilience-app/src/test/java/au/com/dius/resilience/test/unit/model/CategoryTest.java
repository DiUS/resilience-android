package au.com.dius.resilience.test.unit.model;

import au.com.dius.resilience.model.Category;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class CategoryTest {

  @Test
  public void shouldResolveKnownTypes() {
    assertNotNull(Category.asCategory("fire"));

    assertThat(Category.asCategory("something-or-other"), is(Category.UNKNOWN));
  }
}
