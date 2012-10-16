package au.com.dius.resilience.persistence.repository.impl;

import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author georgepapas
 */
public class RepositoryCommandResultTest {

  @Test
  public void hasResultsShouldReturnFalseWhenNoZeroResults() {
    assertFalse(new RepositoryCommandResult<String>(false, Collections.EMPTY_LIST).hasResults());
  }

  @Test
  public void hasResultsShouldReturnFalseWhenNull() {
    assertFalse(new RepositoryCommandResult<String>(false, null).hasResults());
  }

  @Test
  public void hasResultsShouldReturnTrue() {
    assertTrue(new RepositoryCommandResult<String>(false, Arrays.asList(new String[]{"foo"})).hasResults());
  }
}
