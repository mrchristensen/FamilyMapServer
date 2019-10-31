package exceptions;

public class DataAccessException extends Exception {
    public DataAccessException(String message)
    {
        super(message);
    }

    @Override
    public String toString() {
        return getMessage();
    }

    DataAccessException()
    {
        super();
    }
}
