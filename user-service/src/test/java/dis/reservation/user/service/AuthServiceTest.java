package dis.reservation.user.service;

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
    void register_success() {

        RegisterRequestDto req = new RegisterRequestDto("testuser", "pass", Role.ADMIN);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encoded-pass");

        authService.register(req);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();

        assertEquals("testuser", saved.getUsername());
        assertEquals("encoded-pass", saved.getPassword());
        assertEquals(Role.ADMIN, saved.getRole());
    }

    @Test
    void register_usernameAlreadyExists_throwsException() {

        RegisterRequestDto req = new RegisterRequestDto("user", "pass", Role.ADMIN);

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(new User()));

        assertThrows(UserServiceGeneralException.class, () -> authService.register(req));

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_success() {

        LoginRequestDto req = new LoginRequestDto("user", "pass");

        User u = User.builder()
                .username("user")
                .password("encoded")
                .role(Role.ADMIN)
                .build();

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("pass", "encoded")).thenReturn(true);
        when(jwtUtil.generateToken("user", Role.ADMIN)).thenReturn("jwt-token");

        AuthResponseDto response = authService.login(req);

        assertEquals("jwt-token", response.token());
    }

    @Test
    void login_invalidUsername_throwsException() {

        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        LoginRequestDto req = new LoginRequestDto("unknown", "pass");

        assertThrows(UserServiceGeneralException.class, () -> authService.login(req));
    }

    @Test
    void login_wrongPassword_throwsException() {

        LoginRequestDto req = new LoginRequestDto("user", "wrong");

        User u = User.builder()
                .username("user")
                .password("encoded")
                .role(Role.ADMIN)
                .build();

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        assertThrows(UserServiceGeneralException.class, () -> authService.login(req));
    }
}

