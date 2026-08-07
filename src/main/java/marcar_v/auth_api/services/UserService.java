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

import java.time.ZonedDateTime;
import java.time.ZoneId;

@Service
public class UserService {
    private final UserRepository repo;
    private final SessionRepository sessionRepo;
    private final BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
    private final MailService mailService;

    public UserService(UserRepository repo, SessionRepository sessionRepo, MailService mailService){
        this.repo = repo;
        this.sessionRepo = sessionRepo;
        this.mailService = mailService;
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

        if(session.getExpiration().isBefore(ZonedDateTime.now(ZoneId.of("Europe/Madrid")))) {
            throw new InvalidTokenException("Expired token");
        }

        UserEntity user = session.getUser();

        sessionRepo.deleteByUserEmail(user.getEmail());
        repo.delete(user);
    }

    public void recoveryPassword(String email){
        //Comprobar que el usuario existe en bbdd
        //Generar el código y guardarlo hasheado asociado al email en PasswordRecoveryRequest
        //
        mailService.sendPasswordRecoveryEmail(email, "test");
    }

    public void updatePassword(String recoveryCode, String newPassword){
        //Obtener la fila de la tabla de password recovery request en base al código hasheado previamente
        //Comprobar que la fila obtenida no tiene la expiración caducada
        //En base a la fila, obtener el usuario de la bbdd de users
        //Si existe, actualizar su contraseña y cerrar todas las sessiones
    }

}
