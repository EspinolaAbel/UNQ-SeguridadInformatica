package ar.edu.unq.seguridadinformatica.service;

import ar.edu.unq.seguridadinformatica.entity.User;
import ar.edu.unq.seguridadinformatica.entity.UserAuthorizationEnum;
import ar.edu.unq.seguridadinformatica.entity.UserSession;
import ar.edu.unq.seguridadinformatica.repository.UserRepository;
import ar.edu.unq.seguridadinformatica.repository.UserSessionRepository;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final UserSessionRepository sessionRepo;

    public UserService(UserRepository userRepo, UserSessionRepository sessionRepo) {
        this.userRepo = userRepo;
        this.sessionRepo = sessionRepo;
    }

    @PostConstruct
    /** Esto es para inicializar un usuario administrador en el arranque de la aplicación */
    public void createAdmin() {
        var admin = userRepo.findByUsernameAndHashedPassword("admin", passwordHashing("admin"));
        if (admin.isEmpty()) {
            registerUser("admin", "admin", UserAuthorizationEnum.ADMIN);
        }
    }

    public RegisteredUser registerUser(String username, String password) {
        return registerUser(username, password, UserAuthorizationEnum.NORMAL);
    }

    public RegisteredUser registerUser(String username, String password, UserAuthorizationEnum... authorizations) {
        if (userRepo.existsByUsername(username))
            throw UserException.usernameAlreadyExists(username);
        var hashedPassword = passwordHashing(password);
        var createdUser = userRepo.save(User.newUser(username, hashedPassword, authorizations));
        return new RegisteredUser(createdUser);
    }

    private String passwordHashing(String password) {
        var hashedPassword = DigestUtils.sha256Hex(password);
        // TODO: usar salting para el password
        return hashedPassword;
    }

    public UserSession loginUser(String username, String password) {
        var hashedPassword = passwordHashing(password);
        var user = getUserOrThrowException(username, hashedPassword);
        return sessionRepo.save(new UserSession(user));
    }

    public User getUserOrThrowException(String username, String hashedPassword) {
        return userRepo.findByUsernameAndHashedPassword(username, hashedPassword)
            .orElseThrow(() -> UserException.userNotFound(username));
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public List<UserSession> getSessions() {
        return sessionRepo.findAll();
    }

    public void logoutUser(String sessionId) {
        sessionRepo.deleteBySessionId(sessionId);
    }

    public boolean hasAnyAuthorization(String sessionId, UserAuthorizationEnum... autorizations) {
        var session = sessionRepo.findBySessionId(sessionId)
            .orElseThrow(() -> SessionException.sessionDoesNotExists(sessionId));
        var user = userRepo.findById(session.getUserUuid())
            .orElseThrow(() -> UserException.userWithIdNotFound(session.getUserUuid()));
        for (var auth: autorizations) {
            if (user.getAuthorizations().contains(auth))
                return true;
        }
        return false;
    }

    @Data
    public static class RegisteredUser {
        public RegisteredUser(User user) {
            this.uuid = user.getUuid();
            this.username = user.getUsername();
            this.creationTimestamp = user.getCreationTimestamp();
        }
        private UUID uuid;
        private String username;
        private Instant creationTimestamp;
    }
}
