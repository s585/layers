package tech.itpark.exception;

public class UsernameDoesNotExistException extends RuntimeException {
    public UsernameDoesNotExistException() {
    }

    public UsernameDoesNotExistException(String message) {
        super(message);
    }

    public UsernameDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameDoesNotExistException(Throwable cause) {
        super(cause);
    }

    protected UsernameDoesNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}