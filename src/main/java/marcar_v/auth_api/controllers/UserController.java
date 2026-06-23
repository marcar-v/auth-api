package marcar_v.auth_api.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import marcar_v.auth_api.dtos.UserDTOs.*;
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
}
