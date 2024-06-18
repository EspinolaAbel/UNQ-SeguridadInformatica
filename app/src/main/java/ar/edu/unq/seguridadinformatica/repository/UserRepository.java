package ar.edu.unq.seguridadinformatica.repository;

import ar.edu.unq.seguridadinformatica.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsername(String username);
    Optional<User> findByUsernameAndHashedPassword(String username, String hashedPassword);
}
