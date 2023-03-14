package com.example.dispmov.models;

import com.example.dispmov.Videocall;

public class Message {
    private String msg;
    private String name;
    private String hour;
    public Videocall context;
    public Message() {
    }

    public Message(String msg, String name, String hour) {
        this.msg = msg;
        this.name = name;
        this.hour = hour;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.name = hour;
    }

}
