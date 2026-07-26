package marcar_v.auth_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import marcar_v.auth_api.entities.SessionEntity;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<SessionEntity, String>{
    Optional<SessionEntity> findByAuthToken(String authToken);
    Optional<SessionEntity> findByRefreshToken(String refreshToken);
    void deleteByUserEmail(String email);
}
