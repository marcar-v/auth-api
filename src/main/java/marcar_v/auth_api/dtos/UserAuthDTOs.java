package marcar_v.auth_api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;

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
        @JsonFormat(pattern = "yyyy-MM-dd 'T' HH:mm:ssXXX")
        ZonedDateTime expiration
    ) {}

    public record RefreshTokenRequest(
            @NotBlank String refreshToken
    ) {}

    public record PasswordRecoveryRequest(
            @NotBlank @Email String email
    ) {}

    public record UpdatePasswordRequest(
            @NotBlank String password,

            @NotBlank
            @JsonProperty("recovery_code")
            String recoveryCode
    ) {}

    public record AuthCheckResponse(
            boolean valid
    ) {}
}
