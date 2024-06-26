package ar.edu.unq.seguridadinformatica.controller;

import ar.edu.unq.seguridadinformatica.entity.UserAuthorizationEnum;
import ar.edu.unq.seguridadinformatica.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import static ar.edu.unq.seguridadinformatica.controller.UserController.SESSION_ID;

@Controller
@RequestMapping("/")
@Slf4j
public class Pages {

    private final UserService userService;

    public Pages(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @RequestMapping("registrar")
    public String register() {
        return "registrar";
    }

    @RequestMapping("main")
    public String mainPage(@CookieValue(SESSION_ID) String sessionId) {
        return getPageIfUserHasAuthorization(
            "main_page",
            sessionId,
            UserAuthorizationEnum.values() // cualquier usuario logueado
        );
    }

    @RequestMapping("admin")
    public String admin(@CookieValue(SESSION_ID) String sessionId) {
        return getPageIfUserHasAuthorization(
            "admin_page",
            sessionId,
            UserAuthorizationEnum.ADMIN // solo el admin puede ver esta página
        );
    }

    private String getPageIfUserHasAuthorization(String requestedPage, String sessionId, UserAuthorizationEnum... authorizations) {
        return userService.hasAnyAuthorization(sessionId, authorizations)
            ? requestedPage
            : "user_unauthorized_page";
    }

}
