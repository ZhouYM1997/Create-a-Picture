package com.ych.unit;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ych.R;

import java.util.List;


/**
 * 聊天信息适配器

 */
public class ChatMessageAdapter extends BaseAdapter {

    private List<ChatMessage> list;

    public ChatMessageAdapter(List<ChatMessage> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.isEmpty() ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = list.get(position);
        // 如果是接收消息：0，发送消息：1
        if (chatMessage.getType() == ChatMessage.Type.INCOUNT) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessage = list.get(position);
        if (convertView == null) {
            ViewHolder viewHolder = null;
            // 通过ItemType加载不同的布局
            if (getItemViewType(position) == 0) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.left_buju, null);
                viewHolder = new ViewHolder();
                viewHolder.chat_time = (TextView) convertView
                        .findViewById(R.id.chat_left_time);
                viewHolder.chat_message = (TextView) convertView
                        .findViewById(R.id.chat_left_message);
            } else {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.right_buju, null);
                viewHolder = new ViewHolder();
                viewHolder.chat_time = (TextView) convertView
                        .findViewById(R.id.chat_right_time);
                viewHolder.chat_message = (TextView) convertView
                        .findViewById(R.id.chat_right_message);
            }
            convertView.setTag(viewHolder);
        }
        // 设置数据
        ViewHolder vh = (ViewHolder) convertView.getTag();
        vh.chat_time.setText(DateUtils.dateToString(chatMessage.getData()));
        vh.chat_message.setText(chatMessage.getMessage());
        return convertView;
    }

    /**
     * 内部类：只寻找一次控件
     */
    private class ViewHolder {
        private TextView chat_time, chat_message;
    }
}