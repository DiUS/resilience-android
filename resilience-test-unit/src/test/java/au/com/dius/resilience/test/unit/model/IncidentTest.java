package au.com.dius.resilience.test.unit.model;

import au.com.dius.resilience.model.Impact;
import au.com.dius.resilience.model.Incident;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

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
}
