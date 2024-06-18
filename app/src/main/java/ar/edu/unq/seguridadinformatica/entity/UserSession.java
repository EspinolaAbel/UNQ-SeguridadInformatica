package ar.edu.unq.seguridadinformatica.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Data @NoArgsConstructor
public class UserSession {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private UUID userUuid;
    private String sessionId;
    private Instant creationTimestamp;
    private Instant expirationTimestamp;

    public UserSession(User user) {
        this.userUuid = user.getUuid();
        this.creationTimestamp = Instant.now();
        this.expirationTimestamp = creationTimestamp.plus(1, ChronoUnit.HOURS);
        this.sessionId = UUID.randomUUID().toString();
    }
}
