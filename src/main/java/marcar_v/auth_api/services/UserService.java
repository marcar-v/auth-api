package marcar_v.auth_api.services;

import marcar_v.auth_api.entities.SessionEntity;
import marcar_v.auth_api.entities.UserEntity;
import marcar_v.auth_api.repositories.SessionRepository;
import marcar_v.auth_api.services.exceptions.InvalidTokenException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import marcar_v.auth_api.dtos.UserAuthDTOs.*;
import marcar_v.auth_api.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository repo;
    private final SessionRepository sessionRepo;
    private final BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repo, SessionRepository sessionRepo){
        this.repo = repo;
        this.sessionRepo = sessionRepo;
    }

    public CreateUserResponse createUser (CreateUserRequest req){
        String passHash = this.passEncoder.encode(req.passHash());

        UserEntity newUser = new UserEntity(req.email(), passHash);
        UserEntity savedUser = this.repo.save(newUser);

        return new CreateUserResponse(savedUser.getEmail());
    }

    @Transactional
    public void deleteUser(String authToken){
        SessionEntity session = sessionRepo.findByAuthToken(authToken)
                .orElseThrow(() -> new InvalidTokenException("Invalid token")); //Cambiar esto

        if(session.getExpiration().isBefore(LocalDateTime.now())){
            throw new InvalidTokenException("Expired token");
        }

        UserEntity user = session.getUser();

        sessionRepo.deleteByUserEmail(user.getEmail());
        repo.delete(user);
    }

}
