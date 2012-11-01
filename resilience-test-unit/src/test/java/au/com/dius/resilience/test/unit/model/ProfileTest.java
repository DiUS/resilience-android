package au.com.dius.resilience.test.unit.model;

import au.com.dius.resilience.model.Profile;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;

public class ProfileTest {

  @Test
  public void equalsShouldBeReflexive() {
    Profile profile = new Profile("Name1");
    assertTrue(profile.equals(profile));
  }

  @Test
  public void equalsShouldBeSymmetric() {
    Profile profile1 = new Profile("SameName");
    Profile profile2 = new Profile("SameName");

    assertTrue(profile1.equals(profile2));
    assertTrue(profile2.equals(profile1));
  }

  @Test
  public void equalsShouldBeTransitive() {
    Profile profile1 = new Profile("SameName");
    Profile profile2 = new Profile("SameName");
    Profile profile3 = new Profile("SameName");

    assertTrue(profile1.equals(profile2));
    assertTrue(profile2.equals(profile3));
    assertTrue(profile1.equals(profile3));
  }

  @Test
  public void equalsShouldBeFalseForNull() {
    Profile profile1 = new Profile("Name");
    assertFalse(profile1.equals(null));
  }

  @Test
  public void shouldNotBeEqual() {
    Profile profile1 = new Profile("SameName");
    Profile profile2 = new Profile("SomeOtherName");

    assertFalse(profile1.equals(profile2));
  }

  @Test
  public void hashCodeShouldBeEqual() {
    Profile profile1 = new Profile("SameName");
    Profile profile2 = new Profile("SameName");

    assertTrue(profile1.equals(profile2));
    assertEquals(profile1.hashCode(), profile2.hashCode());
  }

  @Test
  public void hashCodeShouldNotBeEqualIfNotEqual() {
    Profile profile1 = new Profile("SameName");
    Profile profile2 = new Profile("SomeOtherName");

    assertFalse(profile1.equals(profile2));
    assertFalse(profile1.hashCode() == profile2.hashCode());
  }
}
