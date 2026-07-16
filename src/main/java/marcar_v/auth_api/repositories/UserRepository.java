package marcar_v.auth_api.repositories;

import marcar_v.auth_api.entities.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import marcar_v.auth_api.entities.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String>{
    Optional<UserEntity> findByEmail(String email);
}
