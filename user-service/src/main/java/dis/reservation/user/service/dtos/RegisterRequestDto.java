package dis.reservation.user.service.dtos;

import dis.reservation.user.service.enums.Role;

public record RegisterRequestDto(String username, String password, Role role) {

}
