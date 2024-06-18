package ar.edu.unq.seguridadinformatica.service;

public class SessionException extends RuntimeException {

    public SessionException(String msg) {
        super(msg);
    }

    public static RuntimeException sessionDoesNotExists(String sessionId) {
        return new SessionException("session="+sessionId+" does not exists");
    }
}
