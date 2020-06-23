package com.example.tellmamobile;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {
    private ArrayList<Chat> chatListDataSet;
    private Context mContext;

    public ChatListAdapter(Context context, ArrayList<Chat> myChatListDataSet) {
        chatListDataSet = myChatListDataSet;
        mContext = context;
    }

    @Override public int getCount() {
        if (chatListDataSet == null || chatListDataSet.size() == 0){
            return 0;
        }

        return chatListDataSet.size();
    }

    @Override public Object getItem(int position) { return chatListDataSet.get(position); }

    @Override public long getItemId(int position) { return (long) chatListDataSet.get(position).getRoomId(); }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        // create a new view
        View view = View.inflate(mContext, R.layout.card_chat_list, null);

        Chat chat = chatListDataSet.get(position);

        TextView textView = (TextView) view.findViewById(R.id.chat_text);
        textView.setText(chat.getName());

        TextView messageTextView = (TextView) view.findViewById(R.id.message_text);
        String subtitle = chat.hasLastMessage() ? String.format("%s: %s", chat.getLastmessagesInfo().getUsername(), chat.getLastmessagesInfo().getText()) : "";
        messageTextView.setText(subtitle);

        TextView dateTextView = (TextView) view.findViewById(R.id.chat_date);
        dateTextView.setText(chat.getLastmessagesInfo().getFormattedDate());

        return view;

    }

    public void setChatListDataSet(ArrayList<Chat> chatListDataSet) {
        this.chatListDataSet = chatListDataSet;
        this.notifyDataSetChanged();
    }
}

