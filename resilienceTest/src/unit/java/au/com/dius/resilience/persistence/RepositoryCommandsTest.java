package au.com.dius.resilience.persistence;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author georgepapas
 */
public class RepositoryCommandsTest {

  @Mock
  private Repository<String> mockRepository;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void findAllShouldBeCalledOnRespository() {
    final RepositoryCommand<String> command = RepositoryCommands.findAll(mockRepository);
    assertNotNull(command);

    command.perform();
    verify(mockRepository).findAll();
  }

  @Test
  public void findAllShouldReturnRepositoryResult() {
    final List<String> results = Arrays.asList(new String[]{"foo","bar"});
    when(mockRepository.findAll()).thenReturn(results);

    assertEquals(results, RepositoryCommands.findAll(mockRepository).perform().getResults());
  }

  @Test
  public void saveShouldBeCalledOnRepository() {
    final String foo = "";
    final RepositoryCommand<String> command = RepositoryCommands.save(mockRepository, foo);
    assertNotNull(command);

    command.perform();
    verify(mockRepository).save(foo);
  }

  @Test
  public void saveShouldReturnRepositoryResult() {
    when(mockRepository.save(anyString())).thenReturn(true);
    assertTrue(RepositoryCommands.save(mockRepository, "").perform().isSuccess());

    when(mockRepository.save(anyString())).thenReturn(false);
    assertFalse(RepositoryCommands.save(mockRepository, "").perform().isSuccess());
  }

  @Test
  public void findByIdShouldBeCalledOnRepository() {
    long id = 1;
    final RepositoryCommand<String> command  = RepositoryCommands.findById(mockRepository, id);

    command.perform();
    verify(mockRepository).findById(id);
  }

  @Test
  public void findByIdShouldReturnFoundItem() {
    long id = 1;
    final String expected = "drWho";

    when(mockRepository.findById(id)).thenReturn(expected);
    final RepositoryCommand<String> command  = RepositoryCommands.findById(mockRepository, id);

    final RepositoryCommandResult<String> result = command.perform();
    assertTrue(result.isSuccess());
    assertEquals(expected, result.getResults().get(0));
  }

  @Test
  public void findByIdShouldReturnFailedResultWhenNothingFound() {
    long id = 1;
    when(mockRepository.findById(id)).thenReturn(null);
    final RepositoryCommand<String> command  = RepositoryCommands.findById(mockRepository, id);

    final RepositoryCommandResult<String> result = command.perform();
    assertFalse(result.isSuccess());
    assertEquals(0, result.getResults().size());
  }
}
