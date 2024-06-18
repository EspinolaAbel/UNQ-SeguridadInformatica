package ar.edu.unq.seguridadinformatica.service;

import java.util.UUID;

public class UserException extends RuntimeException {

    public final UserExceptionEnum error;

    public enum UserExceptionEnum {
        ALREADYEXISTS, NOTFOUND
    }

    public UserException(String msg, UserExceptionEnum error) {
        super(msg);
        this.error = error;
    }

    public static RuntimeException usernameAlreadyExists(String username) {
        return new UserException("User with username="+username+" is already used", UserExceptionEnum.ALREADYEXISTS);
    }

    public static RuntimeException userNotFound(String username) {
        return new UserException("User with username="+username+" was not found", UserExceptionEnum.NOTFOUND);
    }

    public static RuntimeException userWithIdNotFound(UUID userUuid) {
        return new UserException("User with userUuid="+userUuid+" was not found", UserExceptionEnum.NOTFOUND);
    }
}
