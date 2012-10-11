package au.com.dius.resilience.model.test;

import junit.framework.TestCase;
import au.com.dius.resilience.model.ImpactScale;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.IncidentFactory;

public class IncidentFactoryTest extends TestCase{

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
