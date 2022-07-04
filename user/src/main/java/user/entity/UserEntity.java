package user.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    private UUID uuid;
    @Column(unique = true)
    private String login;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;

    public UserEntity() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
