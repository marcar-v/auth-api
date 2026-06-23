package marcar_v.auth_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import marcar_v.auth_api.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String>{
}
