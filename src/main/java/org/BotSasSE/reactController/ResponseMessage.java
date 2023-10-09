package org.BotSasSE.reactController;

public class ResponseMessage {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public ResponseMessage(String message) {
        this.message = message;
    }


}
