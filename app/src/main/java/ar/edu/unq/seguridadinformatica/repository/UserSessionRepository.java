package ar.edu.unq.seguridadinformatica.repository;

import ar.edu.unq.seguridadinformatica.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {
    void deleteBySessionId(String sessionId);
    Optional<UserSession> findBySessionId(String sessionId);
}
