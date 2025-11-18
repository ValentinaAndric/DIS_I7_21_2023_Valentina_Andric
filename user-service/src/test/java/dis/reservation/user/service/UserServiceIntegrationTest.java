package dis.reservation.user.service;

import static dis.reservation.user.service.UserServiceDataFixture.TEST_PASSWORD;
import static dis.reservation.user.service.UserServiceDataFixture.TEST_PASSWORD_SECOND;
import static dis.reservation.user.service.UserServiceDataFixture.TEST_USERNAME;
import static dis.reservation.user.service.UserServiceDataFixture.TEST_USERNAME_SECOND;
import static org.assertj.core.api.Assertions.assertThat;

import dis.reservation.user.service.entity.User;
import dis.reservation.user.service.enums.Role;
import dis.reservation.user.service.repository.UserRepository;
import dis.reservation.user.service.service.UserService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceIntegrationTest implements PostgresTestContainer {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {

        userRepository.deleteAll();
    }

    @Test
    void testShouldSaveUser() {

        User user = User.builder()
                .username(TEST_USERNAME)
                .role(Role.USER)
                .build();

        User saved = userService.saveUser(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(saved.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void testShouldGetUserById() {

        User user = User.builder()
                .username(TEST_USERNAME)
                .role(Role.USER)
                .build();

        User saved = userService.saveUser(user);
        User found = userService.getUserById(saved.getId());

        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(found.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void testShouldReturnAllUsers() {

        userRepository.save(new User(null, TEST_USERNAME, TEST_PASSWORD, Role.USER));
        userRepository.save(new User(null, TEST_USERNAME_SECOND, TEST_PASSWORD_SECOND, Role.USER));

        List<User> users = userService.getAllUsers();

        assertThat(users).hasSize(2);
    }

    @Test
    void testShouldDeleteUser() {

        User u = userService.saveUser(new User(null, TEST_USERNAME, TEST_PASSWORD, Role.USER));

        userService.deleteUser(u.getId());

        assertThat(userRepository.findById(u.getId())).isEmpty();
    }
}

