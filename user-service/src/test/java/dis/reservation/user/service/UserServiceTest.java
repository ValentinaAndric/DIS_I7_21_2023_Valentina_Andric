package dis.reservation.user.service;

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
    void getAllUsers_returnsList() {

        List<User> users = Arrays.asList(
                User.builder().username("a").build(),
                User.builder().username("b").build()
        );

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_found() {

        User u = User.builder()
                .id(1L)
                .username("test")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        User result = userService.getUserById(1L);

        assertEquals("test", result.getUsername());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_notFound_throwsException() {

        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> userService.getUserById(10L));
    }

    @Test
    void saveUser_success() {

        User u = User.builder().username("test").build();

        when(userRepository.save(u)).thenReturn(u);

        User saved = userService.saveUser(u);

        assertEquals("test", saved.getUsername());
        verify(userRepository).save(u);
    }

    @Test
    void deleteUser_success() {

        userService.deleteUser(5L);

        verify(userRepository).deleteById(5L);
    }
}
