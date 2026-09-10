package marcar_v.auth_api.repositories;

import marcar_v.auth_api.entities.RecoveryCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecoveryRequestRepository extends JpaRepository<RecoveryCodeEntity, String> {
}
