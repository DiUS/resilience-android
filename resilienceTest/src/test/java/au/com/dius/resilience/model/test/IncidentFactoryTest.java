package au.com.dius.resilience.model.test;

import junit.framework.TestCase;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.IncidentFactory;

public class IncidentFactoryTest extends TestCase{

	public void testCreateWithNameAndDateCreated() {
		Long dateCreated = Long.valueOf(1000);
		String name = "incident";
		String note = "The monkeys are armed with nunchucks!";
		Incident incident = IncidentFactory.createIncident(name, dateCreated, note);
		
		assertEquals(name, incident.getName());
		assertEquals(dateCreated, incident.getDateCreated());
		assertEquals(note, incident.getNote());
	}
}
