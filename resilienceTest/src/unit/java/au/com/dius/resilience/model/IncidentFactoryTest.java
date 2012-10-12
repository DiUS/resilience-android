package au.com.dius.resilience.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class  IncidentFactoryTest {

  @Test
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
