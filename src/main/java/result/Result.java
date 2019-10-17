package result;

public class Result {

    /**
     * Message that will predominantly be used as the error response handling
     */
    String message;

    /**
     * Default constructor
     */
    public Result() {
    }

    /**
     * Proper constructor to generate a general response message
     * @param message the string that will be interpreted as the message
     */
    public Result(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
