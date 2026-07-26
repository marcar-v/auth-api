package marcar_v.auth_api.controllers;

import jakarta.validation.Valid;
import marcar_v.auth_api.dtos.UserAuthDTOs;
import marcar_v.auth_api.services.AuthService;
import org.springframework.boot.convert.PeriodUnit;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service){
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public UserAuthDTOs.AuthResponse login (@Valid @RequestBody UserAuthDTOs.AuthRequest req){
        return this.service.login(req);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserAuthDTOs.AuthResponse refresh(@Valid @RequestBody UserAuthDTOs.RefreshTokenRequest req) {
        return this.service.refresh(req);
    }
}
