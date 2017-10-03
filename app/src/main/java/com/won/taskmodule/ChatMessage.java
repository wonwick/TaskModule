package com.won.taskmodule;

/**
 * Created by Oshan Wickramaratne on 2017-09-06.
 */

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ChatMessage {
    private String message;
    private String sender;
    private String time;

    public ChatMessage() {


    }

    public ChatMessage(String message, String sender, String time) {
        this.message = message;
        this.sender = sender;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getTime() {
        return time;
    }
}
