package marcar_v.auth_api.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
public class RecoveryCodeEntity {

    @Id
    private String email;

    @OneToOne
    @MapsId
    @JoinColumn(name = "email", referencedColumnName = "email")
    private UserEntity user;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "expiresAt", nullable = false)
    private ZonedDateTime expiresAt;

    protected RecoveryCodeEntity() {    }

    public RecoveryCodeEntity(UserEntity user, String code, ZonedDateTime createdAt, ZonedDateTime expiresAt) {
        this.user = user;
        this.code = code;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public ZonedDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(ZonedDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
