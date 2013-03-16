package au.com.dius.resilience.test.unit.model;

import au.com.dius.resilience.model.Impact;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.model.Point;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class IncidentTest {

  @Test
	public void testConstructor() {
		Long dateCreated = Long.valueOf(1000);
		String name = "incident";
		String note = "The monkeys are armed with nunchucks!";
		String category = "Monkey Attack";
		String subCategory = "Rabid Monkeys";
		Incident incident = new Incident(name, dateCreated, note, category, subCategory, Impact.HIGH);

		assertEquals(name, incident.getName());
		assertEquals(dateCreated, incident.getDateCreated());
		assertEquals(note, incident.getNote());
		assertEquals(category, incident.getCategory());
		assertEquals(subCategory, incident.getSubCategory());
		assertEquals(Impact.HIGH, incident.getImpact());
	}

  @Test
  public void testHasPhotos() {
    assertThat(new Incident().hasPhotos(), is(false));

    final Incident withPhotos = new Incident();
    List<Photo> photos = new ArrayList<Photo>();
    photos.add(new Photo(null, null));
    withPhotos.addPhotos(photos);

    assertThat(withPhotos.hasPhotos(), is(true));
  }

  @Test
  public void shouldSetLocation() {
    Point point = new Point(10, 10);

    Incident incident = new Incident();
    incident.setPoint(point);

    assertThat(incident.getPoint(), sameInstance(point));
  }
}
