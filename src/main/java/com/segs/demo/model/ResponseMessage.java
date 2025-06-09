package com.segs.demo.model;

public class ResponseMessage {
    private String message;

    public String getMessage() {
        return message;
    }
    public ResponseMessage(){
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseMessage(String message) {
        super();
        this.message = message;
    }
    
}
