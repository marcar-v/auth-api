package marcar_v.auth_api.services;

import marcar_v.auth_api.entities.RecoveryCodeEntity;
import marcar_v.auth_api.entities.SessionEntity;
import marcar_v.auth_api.entities.UserEntity;
import marcar_v.auth_api.repositories.RecoveryRequestRepository;
import marcar_v.auth_api.repositories.SessionRepository;
import marcar_v.auth_api.services.exceptions.InvalidTokenException;
import org.apache.catalina.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import marcar_v.auth_api.dtos.UserAuthDTOs.*;
import marcar_v.auth_api.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    private final SessionRepository sessionRepo;
    private final RecoveryRequestRepository recoveryCodeRepo;
    private final BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
    private final MailService mailService;

    public UserService(UserRepository repo, SessionRepository sessionRepo, RecoveryRequestRepository recoveryCodeRepo, MailService mailService){
        this.repo = repo;
        this.sessionRepo = sessionRepo;
        this.recoveryCodeRepo = recoveryCodeRepo;
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

    @Transactional
    public void recoveryPassword(String email){
        Optional<UserEntity> userOpt = repo.findByEmail(email);
        if(userOpt.isEmpty()){
            System.out.println("User not found");
            return;
        }

        UserEntity user = userOpt.get();

        if(recoveryCodeRepo.existsById(email)){
            return;
        }

        String code = generateRecoveryCode();
        String hashedCode = passEncoder.encode(code);

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Madrid"));
        RecoveryCodeEntity entity = new RecoveryCodeEntity(user, hashedCode, now, now.plusMinutes(15));
        recoveryCodeRepo.save(entity);

        System.out.println("Email sent");

        mailService.sendPasswordRecoveryEmail(email, code);
    }

    private String generateRecoveryCode(){
        SecureRandom random = new SecureRandom();
        int code = 10000 + random.nextInt(90000);
        return String.valueOf(code);
    }

    @Transactional
    public void updatePassword(String recoveryCode, String newPassword){
        Optional<RecoveryCodeEntity> matchOpt = recoveryCodeRepo.findAll().stream()
                .filter(entity -> passEncoder.matches(recoveryCode, entity.getCode()))
                .findFirst();

        if(matchOpt.isEmpty()){
            return;
        }

        RecoveryCodeEntity recoveryCodeEntity = matchOpt.get();

        recoveryCodeRepo.delete(recoveryCodeEntity);

        if(recoveryCodeEntity.getExpiresAt().isBefore(ZonedDateTime.now(ZoneId.of("Europe/Madrid")))){
            return;
        }

        UserEntity user = recoveryCodeEntity.getUser();
        user.setPassHash(passEncoder.encode(newPassword));
        repo.save(user);

        sessionRepo.deleteByUserEmail(user.getEmail());
    }

}
