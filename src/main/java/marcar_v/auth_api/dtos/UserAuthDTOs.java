package marcar_v.auth_api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class UserAuthDTOs {

    public record CreateUserRequest(
            @NotBlank @Email @Size(max = 255) String email,
            @NotBlank @Size(min = 8, max = 64) String passHash
    ) {}

    public record CreateUserResponse(
            String email
    ) {}

    public record AuthRequest(
            @NotBlank @Email String email,
            @NotBlank @Size (min=8, max = 64) String passHash
    ) {}

    public record AuthResponse(
        String authToken,
        String refreshToken,
        LocalDateTime expiration
    ) {}
}
