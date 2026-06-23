package marcar_v.auth_api.entities;

import jakarta.persistence.*;
import org.apache.catalina.User;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_users_email", columnNames = "email")
        }
)

public class UserEntity {

    @Id
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "passHash", nullable = false, length = 64)
    private String passHash;

    protected UserEntity(){}

    public UserEntity (String email, String passHash){
        this.email = email;
        this.passHash = passHash;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassHash(){
        return passHash;
    }

    public void setPassHash(){
        this.passHash = passHash;
    }
}



