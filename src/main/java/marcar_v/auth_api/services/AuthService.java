package marcar_v.auth_api.services;


import marcar_v.auth_api.dtos.UserAuthDTOs;
import marcar_v.auth_api.entities.*;
import marcar_v.auth_api.repositories.*;
import marcar_v.auth_api.services.exceptions.InvalidCredentialsException;
import marcar_v.auth_api.services.exceptions.InvalidTokenException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final SessionRepository sessionRepo;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepo, SessionRepository sessionRepo, JwtService jwtService) {
        this.userRepo = userRepo;
        this.sessionRepo = sessionRepo;
        this.jwtService = jwtService;
    }

    public UserAuthDTOs.AuthResponse login(UserAuthDTOs.AuthRequest req){
        UserEntity user = userRepo.findByEmail(req.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if(!passEncoder.matches(req.passHash(), user.getPassHash())){
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String authToken = jwtService.generateToken(user.getEmail());
        String refreshToken = UUID.randomUUID().toString();
        ZonedDateTime expiration = jwtService.calculateExpiration();

        SessionEntity session = new SessionEntity(
                UUID.randomUUID().toString(),
                user,
                authToken,
                refreshToken,
                expiration
        );
        sessionRepo.save(session);

        return new UserAuthDTOs.AuthResponse(authToken, refreshToken, expiration);
    }

    @Transactional
    public UserAuthDTOs.AuthResponse refresh (UserAuthDTOs.RefreshTokenRequest req){
        SessionEntity session = sessionRepo.findByRefreshToken(req.refreshToken())
                .orElseThrow(()-> new InvalidTokenException("Invalid refresh token"));

        String newAuthToken = jwtService.generateToken(session.getUser().getEmail());
        String newRefreshToken = UUID.randomUUID().toString();
        ZonedDateTime newExpiration = jwtService.calculateExpiration();

        session.setAuthToken(newAuthToken);
        session.setRefreshToken(newRefreshToken);
        session.setExpiration(newExpiration);

        sessionRepo.save(session);

        return new UserAuthDTOs.AuthResponse(newAuthToken, newRefreshToken, newExpiration);
    }
}
