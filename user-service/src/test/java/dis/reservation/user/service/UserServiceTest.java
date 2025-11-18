package dis.reservation.user.service;

import static dis.reservation.user.service.UserServiceDataFixture.TEST_USERNAME;
import static dis.reservation.user.service.UserServiceDataFixture.TEST_USERNAME_SECOND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dis.reservation.user.service.entity.User;
import dis.reservation.user.service.repository.UserRepository;
import dis.reservation.user.service.service.UserService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);

    @Test
    void testGetAllUsers_shouldReturnsList() {

        List<User> users = Arrays.asList(
                User.builder().username(TEST_USERNAME).build(),
                User.builder().username(TEST_USERNAME_SECOND).build()
        );

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_found() {

        User u = User.builder()
                .id(1L)
                .username(TEST_USERNAME)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        User result = userService.getUserById(1L);

        assertEquals(TEST_USERNAME, result.getUsername());
        verify(userRepository).findById(1L);
    }

    @Test
    void testGetUserById_notFound_throwsException() {

        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> userService.getUserById(10L));
    }

    @Test
    void testSaveUser_success() {

        User u = User.builder().username(TEST_USERNAME).build();

        when(userRepository.save(u)).thenReturn(u);

        User saved = userService.saveUser(u);

        assertEquals(TEST_USERNAME, saved.getUsername());
        verify(userRepository).save(u);
    }

    @Test
    void testDeleteUser_success() {

        userService.deleteUser(5L);

        verify(userRepository).deleteById(5L);
    }
}
