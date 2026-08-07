package marcar_v.auth_api.controllers;

import jakarta.validation.Valid;
import marcar_v.auth_api.services.exceptions.InvalidTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import marcar_v.auth_api.dtos.UserAuthDTOs.*;
import marcar_v.auth_api.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    public UserController(UserService service){
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUserResponse create(@Valid @RequestBody CreateUserRequest req){
        return this.service.createUser(req);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@RequestHeader("Authorization") String authHeader){
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new InvalidTokenException("Token not provided");
        }

        String authToken = authHeader.replaceFirst("^Bearer ", "");

        this.service.deleteUser(authToken);
    }

    @PostMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public void recoveryPassword(@Valid @RequestBody PasswordRecoveryRequest request){
        this.service.recoveryPassword(request.email());
    }
}
