package marcar_v.auth_api.services;

import marcar_v.auth_api.entities.UserEntity;
import org.apache.catalina.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import marcar_v.auth_api.dtos.UserDTOs.*;
import marcar_v.auth_api.entities.UserEntity.*;
import marcar_v.auth_api.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository repo;
    private final BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repo){
        this.repo = repo;
    }

    public CreateUserResponse createUser (CreateUserRequest req){
        String passHash = this.passEncoder.encode(req.passHash());

        UserEntity newUser = new UserEntity(req.email(), passHash);
        UserEntity savedUser = this.repo.save(newUser);

        return new CreateUserResponse(savedUser.getEmail());
    }
}
