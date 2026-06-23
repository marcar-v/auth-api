package marcar_v.auth_api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.aspectj.weaver.ast.Not;

public class UserDTOs {

    public record CreateUserRequest(
            @NotBlank @Email @Size(max = 255) String email,
            @NotBlank @Size(min = 8, max = 64) String passHash
    ) {}

    public record CreateUserResponse(
            String email
    ) {}
}
