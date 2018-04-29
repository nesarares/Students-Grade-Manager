package Repository;

public class RepositoryException extends Exception {
    private String message;

    public RepositoryException(String message) {
        super(message);
    }
}
