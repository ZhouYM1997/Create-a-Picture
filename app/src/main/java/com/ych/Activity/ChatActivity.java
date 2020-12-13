package com.ych.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ych.R;
import com.ych.unit.ChatMessage;
import com.ych.unit.ChatMessageAdapter;
import com.ych.unit.HttpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
        private List<ChatMessage> list;
        private ListView chat_listview;
        private EditText chat_input;
        private Button chat_send;
        private ChatMessageAdapter chatAdapter;
        private ChatMessage chatMessage = null;
    int i=0;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_chat);
            initView();
            initListener();
            initData();
        }

        // 1.初始试图
        private void initView() {
            // 1.初始化
            chat_listview = (ListView) findViewById(R.id.chat_listview);
            chat_input = (EditText) findViewById(R.id.chat_input_message);
            chat_send = (Button) findViewById(R.id.chat_send);
        }

        // 2.设置监听事件
        private void initListener() {
            chat_send.setOnClickListener(onClickListener);
        }

        // 3.初始化数据
        private void initData() {
            list = new ArrayList<ChatMessage>();
            list.add(new ChatMessage("您好,小画为您服务。您可以将您的需求或者对我们的意见在此对话框中发送给我们，我们的后台工作人员会及时看到消息并处理。若要开始闲聊模式，请输入“闲聊”或者“聊天”", ChatMessage.Type.INCOUNT, new Date()));
            chatAdapter = new ChatMessageAdapter(list);
            chat_listview.setAdapter(chatAdapter);
            chatAdapter.notifyDataSetChanged();
        }

        // 4.发送消息聊天
        private void chat() {
            // 1.判断是否输入内容
            final String send_message = chat_input.getText().toString().trim();
            if (TextUtils.isEmpty(send_message)) {
                Toast.makeText(ChatActivity.this, "对不起，您还未发送任何消息",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if(i==0) {
                if(send_message.equals("闲聊")||send_message.equals("聊天"))
                {
                    i++;
                }
                if (!send_message.equals("闲聊") && !send_message.equals("聊天")) {

                    ChatMessage sendChatMessage = new ChatMessage();
                    sendChatMessage.setMessage(send_message);
                    sendChatMessage.setData(new Date());
                    sendChatMessage.setType(ChatMessage.Type.OUTCOUNT);
                    list.add(sendChatMessage);
                    chatAdapter.notifyDataSetChanged();
                    chat_input.setText("");
                    return;
                }
            }

            // 2.自己输入的内容也是一条记录，记录刷新
            ChatMessage sendChatMessage = new ChatMessage();
            sendChatMessage.setMessage(send_message);
            sendChatMessage.setData(new Date());
            sendChatMessage.setType(ChatMessage.Type.OUTCOUNT);
            list.add(sendChatMessage);
            chatAdapter.notifyDataSetChanged();
            chat_input.setText("");

            // 3.发送你的消息，去服务器端，返回数据
            new Thread() {
                public void run() {
                    ChatMessage chat = HttpUtils.sendMessage(send_message);
                    Message message = new Message();
                    message.what = 0x1;
                    message.obj = chat;
                    handler.sendMessage(message);
                };
            }.start();
        }

        @SuppressLint("HandlerLeak")
        private Handler handler = new Handler() {

            public void handleMessage(android.os.Message msg) {
                if (msg.what == 0x1) {
                    if (msg.obj != null) {
                        chatMessage = (ChatMessage) msg.obj;
                    }
                    // 添加数据到list中，更新数据
                    list.add(chatMessage);
                    chatAdapter.notifyDataSetChanged();
                }
            };
        };

        // 点击事件监听
        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.chat_send:
                        chat();
                        break;
                }
            }
        };
    }
