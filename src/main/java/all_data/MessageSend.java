package all_data;

import java.io.Serializable;

public class MessageSend implements Serializable {
    private final String message;
    private final Object thePackage;

    public MessageSend(String message, Object thePackage) {
        this.message = message;
        this.thePackage = thePackage;
    }

    public String getMessage() {
        return message;
    }

    public Object getThePackage() {
        return thePackage;
    }
}
