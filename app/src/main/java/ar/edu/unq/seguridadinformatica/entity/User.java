package ar.edu.unq.seguridadinformatica.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity @Table(name = "'User'")
@Data
public class User {

    public User() {
        this.authorizations = new HashSet<>();
    }

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    private String username;
    private String hashedPassword;
    private Instant creationTimestamp;
    @Enumerated
    private Set<UserAuthorizationEnum> authorizations;

    public static User newUser(String username, String password, UserAuthorizationEnum... authorizations) {
        var u = new User();
        u.setUsername(username);
        u.setHashedPassword(password);
        u.setCreationTimestamp(Instant.now());
        u.getAuthorizations().addAll(Arrays.stream(authorizations).toList());
        return u;
    }

}
