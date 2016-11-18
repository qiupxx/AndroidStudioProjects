package com.example.peixuan.uibestpractice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ListView msgListView;
    private EditText inputText;
    private Button send;
    private MsgAdapter adapter;

    private List<Msg> msgList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMsgs();
        adapter = new MsgAdapter(MainActivity.this, R.layout.msg_item, msgList);
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgListView = (ListView) findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyDataSetChanged();//有新消息时，刷新ListView显示
                    msgListView.setSelection(msgList.size());
                    inputText.setText("");
                }
            }
        });
    }

    //程序还不具有联网的能力，预定义号接收方消息
    private void initMsgs() {
        Msg msg1 = new Msg("hello guy.", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("hello. who is that?", Msg.TYPE_RECEIVED);
        msgList.add(msg2);
        Msg msg3 = new Msg("this is tom. nice talking to you", Msg.TYPE_RECEIVED);
        msgList.add(msg3);
    }

}
