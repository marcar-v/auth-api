package marcar_v.auth_api.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Table(
        name = "sessions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_sessions_id", columnNames = "id")
        }
)
public class SessionEntity {

    @Id
    @Column(name = "sessionID", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private UserEntity user;

    @Column(name = "authToken", nullable = false)
    private String authToken;

    @Column(name = "refreshToken", nullable = false)
    private String refreshToken;

    @Column(name = "expiration", nullable = false, length = 32)
    private ZonedDateTime expiration;

    protected SessionEntity(){}

    public SessionEntity (String id, UserEntity user, String authToken, String refreshToken, ZonedDateTime expiration){
        this.id = id;
        this.user = user;
        this.authToken = authToken;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public ZonedDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(ZonedDateTime expiration) {
        this.expiration = expiration;
    }
}
