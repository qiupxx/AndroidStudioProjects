package com.example.peixuan.uibestpractice;

/**
 * Created by peixuan on 16/8/6.
 */
public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;
    //消息内容
    private String content;

    //消息类型，TYPE_RECEIVED/TYPE_SENT之一
    private int type;

    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
