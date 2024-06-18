package ar.edu.unq.seguridadinformatica.controller;

import ar.edu.unq.seguridadinformatica.entity.User;
import ar.edu.unq.seguridadinformatica.entity.UserSession;
import ar.edu.unq.seguridadinformatica.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@RestController @RequestMapping("users")
@Slf4j
public class UserController {

    public static final String SESSION_ID = "session_id";
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<Void> registerUser(@RequestParam("username") String username, @RequestParam("password") String password) {
        log.info("registerUser - request to register user with name {}", username);
        var registeredUser = userService.registerUser(username, password);
        log.info("registerUser - user with name={} was registered with uuid={}", username, registeredUser.getUuid());
        return loginUser(username, password);
    }

    @PostMapping("login")
    public ResponseEntity<Void> loginUser(@RequestParam("username") String username, @RequestParam("password") String password) {
        log.info("loginUser - request to login user with name {}", username);
        var loggedInUser = userService.loginUser(username, password);

        var sessionIdCookie = ResponseCookie.from(SESSION_ID, loggedInUser.getSessionId())
            .httpOnly(true)
            .path("/")
            .maxAge(Duration.between(loggedInUser.getExpirationTimestamp(), Instant.now()).getSeconds())
            .build();

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, sessionIdCookie.toString());

        headers.setLocation(URI.create("/main")); // si el login es exitoso, redirige a /main
        log.info("loginUser - user with name={} uuid={} was successfully logged in", username, loggedInUser.getUserUuid());
        return ResponseEntity.status(HttpStatus.FOUND)
            .headers(headers)
            .build();
    }

    @GetMapping("logout")
    public ResponseEntity<Void> logoutUser(@CookieValue(SESSION_ID) String sessionId) {
        log.info("logoutUser - request to logout user with session={}", sessionId);
        userService.logoutUser(sessionId);
        log.info("logoutUser - user with session={} was successfully logged out", sessionId);
        return ResponseEntity.status(HttpStatus.FOUND)
            .header(HttpHeaders.LOCATION, "/")
            .build();
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("getAllUsers - request");
        var allUsers = userService.getAll();
        log.info("getAllUsers - response, {} uses found", allUsers.size());
        return allUsers;
    }

    @GetMapping("sessions")
    public List<UserSession> getSessions() {
        log.info("getSessions - request");
        var allSessions = userService.getSessions();
        log.info("getSessions - response, {} uses found", allSessions.size());
        return allSessions;
    }

}
