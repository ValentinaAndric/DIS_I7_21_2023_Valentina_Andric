package dis.reservation.user.service;

import static dis.reservation.user.service.UserServiceDataFixture.INVALID_PASSWORD;
import static dis.reservation.user.service.UserServiceDataFixture.TEST_PASSWORD;
import static dis.reservation.user.service.UserServiceDataFixture.TEST_USERNAME;
import static dis.reservation.user.service.UserServiceDataFixture.UNKNOWN_PASSWORD;
import static dis.reservation.user.service.UserServiceDataFixture.UNKNOWN_USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dis.reservation.user.service.config.JwtUtil;
import dis.reservation.user.service.dtos.AuthResponseDto;
import dis.reservation.user.service.dtos.LoginRequestDto;
import dis.reservation.user.service.dtos.RegisterRequestDto;
import dis.reservation.user.service.entity.User;
import dis.reservation.user.service.enums.Role;
import dis.reservation.user.service.exception.UserServiceGeneralException;
import dis.reservation.user.service.repository.UserRepository;
import dis.reservation.user.service.service.AuthService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final JwtUtil jwtUtil = mock(JwtUtil.class);

    private final AuthService authService = new AuthService(
            userRepository, passwordEncoder, jwtUtil
    );

    @Test
    void testRegister_success() {

        RegisterRequestDto req = new RegisterRequestDto(TEST_USERNAME, TEST_PASSWORD, Role.ADMIN);

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn("encoded-pass");

        authService.register(req);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();

        assertEquals(TEST_USERNAME, saved.getUsername());
        assertEquals("encoded-pass", saved.getPassword());
        assertEquals(Role.ADMIN, saved.getRole());
    }

    @Test
    void testRegister_usernameAlreadyExists_throwsException() {

        RegisterRequestDto req = new RegisterRequestDto(TEST_USERNAME, TEST_PASSWORD, Role.ADMIN);

        when(userRepository.findByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(new User()));

        assertThrows(UserServiceGeneralException.class, () -> authService.register(req));

        verify(userRepository, never()).save(any());
    }

    @Test
    void testLogin_success() {

        LoginRequestDto req = new LoginRequestDto(TEST_USERNAME, TEST_PASSWORD);

        User u = User.builder()
                .username(TEST_USERNAME)
                .password("encoded")
                .role(Role.ADMIN)
                .build();

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(u));
        when(passwordEncoder.matches(TEST_PASSWORD, "encoded")).thenReturn(true);
        when(jwtUtil.generateToken(TEST_USERNAME, Role.ADMIN)).thenReturn("jwt-token");

        AuthResponseDto response = authService.login(req);

        assertEquals("jwt-token", response.token());
    }

    @Test
    void testLogin_invalidUsername_throwsException() {

        when(userRepository.findByUsername(UNKNOWN_USERNAME)).thenReturn(Optional.empty());

        LoginRequestDto req = new LoginRequestDto(UNKNOWN_USERNAME, UNKNOWN_PASSWORD);

        assertThrows(UserServiceGeneralException.class, () -> authService.login(req));
    }

    @Test
    void testLogin_wrongPassword_throwsException() {

        LoginRequestDto req = new LoginRequestDto(TEST_USERNAME, INVALID_PASSWORD);

        User u = User.builder()
                .username(TEST_USERNAME)
                .password("encoded")
                .role(Role.ADMIN)
                .build();

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(u));
        when(passwordEncoder.matches(INVALID_PASSWORD, "encoded")).thenReturn(false);

        assertThrows(UserServiceGeneralException.class, () -> authService.login(req));
    }
}

