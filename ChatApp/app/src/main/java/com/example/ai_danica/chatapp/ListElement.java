package com.example.ai_danica.chatapp;

public class ListElement {
    ListElement() {};

    ListElement(String tl, String id, String ts, String name, boolean you) {
        textMessage = tl;
        user_id = id;
        timestamp = ts;
        nickname = name;
        this.you = you;
       // message_id = msgID;
    }

    public String textMessage;
    public String user_id;
    public String timestamp;
    public String nickname;
    public float message_id;
    public boolean you;
}
