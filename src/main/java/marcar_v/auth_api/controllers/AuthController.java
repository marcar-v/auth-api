package marcar_v.auth_api.controllers;

import jakarta.validation.Valid;
import marcar_v.auth_api.dtos.UserAuthDTOs;
import marcar_v.auth_api.services.AuthService;
import marcar_v.auth_api.services.exceptions.InvalidTokenException;
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

    @GetMapping("/auth")
    @ResponseStatus(HttpStatus.OK)
    public UserAuthDTOs.AuthCheckResponse checkAuthToken(@RequestHeader("Authorization") String authHeader){
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new InvalidTokenException("Token not provided");
        }
        String authToken = authHeader.replaceFirst("^Bearer ", "");
        
        boolean isValid = this.service.checkAuthToken(authToken);
        return new UserAuthDTOs.AuthCheckResponse(isValid);
    }

    @DeleteMapping("/auth")
    @ResponseStatus(HttpStatus.OK)
    public void closeSession(@RequestHeader("Authorization") String authHeader) {
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new InvalidTokenException("Token not provided");
        }

        String authToken = authHeader.replaceFirst("^Bearer ", "");

        this.service.closeSession(authToken);
    }
}
