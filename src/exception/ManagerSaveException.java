package exception;

public class ManagerSaveException extends RuntimeException {
    private final String message;

    public ManagerSaveException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
