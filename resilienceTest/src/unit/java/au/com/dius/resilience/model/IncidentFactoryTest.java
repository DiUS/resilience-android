package au.com.dius.resilience.model;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class IncidentFactoryTest {

  @Test
	public void testCreateWithNameAndDateCreated() {
		Long dateCreated = Long.valueOf(1000);
		String name = "incident";
		String note = "The monkeys are armed with nunchucks!";
		String category = "Monkey Attack";
		String subCategory = "Rabid Monkeys";
		Incident incident = IncidentFactory.createIncident(name, dateCreated, note, category, subCategory, ImpactScale.HIGH);
		
		assertEquals(name, incident.getName());
		assertEquals(dateCreated, incident.getDateCreated());
		assertEquals(note, incident.getNote());
		assertEquals(category, incident.getCategory());
		assertEquals(subCategory, incident.getSubCategory());
		assertEquals(ImpactScale.HIGH, incident.getImpact());
	}
}
