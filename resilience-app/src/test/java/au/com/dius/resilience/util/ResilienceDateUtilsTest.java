package au.com.dius.resilience.util;

import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(ResilienceTestRunner.class)
public class ResilienceDateUtilsTest {

  private ResilienceDateUtils resilienceDateUtils;

  @Before
  public void setup() {
    resilienceDateUtils = new ResilienceDateUtils();
  }

  @Test
  public void shouldReturnFilenameFriendlyDate() {
    Date date = new Date(113, 4, 8, 19, 49, 59);

    String formattedDate = resilienceDateUtils.formatFilenameFriendly(date);
    assertThat(formattedDate, is("20130508_194959"));
  }
}