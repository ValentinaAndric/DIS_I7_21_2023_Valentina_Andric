package dis.reservation.user.service.service;

import dis.reservation.user.service.config.JwtUtil;
import dis.reservation.user.service.dtos.AuthResponseDto;
import dis.reservation.user.service.dtos.LoginRequestDto;
import dis.reservation.user.service.dtos.RegisterRequestDto;
import dis.reservation.user.service.entity.User;
import dis.reservation.user.service.exception.ErrorCode;
import dis.reservation.user.service.exception.UserServiceGeneralException;
import dis.reservation.user.service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequestDto request) {

        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new UserServiceGeneralException("Username already exists", ErrorCode.INTERNAL_SERVER_ERROR);
        }

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .build();

        userRepository.save(user);
    }

    public AuthResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UserServiceGeneralException("Invalid username or password",
                                                                   ErrorCode.INTERNAL_SERVER_ERROR));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UserServiceGeneralException("Invalid username or password", ErrorCode.INTERNAL_SERVER_ERROR);
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new AuthResponseDto(token);
    }
}

