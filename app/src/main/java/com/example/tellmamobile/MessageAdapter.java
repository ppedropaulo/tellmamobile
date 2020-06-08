package com.example.tellmamobile;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class MessageAdapter extends BaseAdapter {

    private final Context mcontext;

    private ArrayList<Message> messages;

    public MessageAdapter(Context context, ArrayList<Message> messageList) {
        mcontext = context;
        messages = messageList;
    }

    @Override public int getCount() {
        if (messages == null || messages.size() == 0){
            return 0;
        }

        return messages.size();
    }

    @Override public Message getItem(int position) { return messages.get(position); }

    @Override public long getItemId(int position) { return (long) messages.get(position).getId(); }

    @Override public View getView(int position, View view, ViewGroup parent) {

        View rowView;

        Message message = messages.get(position);

        String messageUserName = message.getUsername();
        String currentUserName = UserSession.getInstance().getUsername();

        if (messageUserName.equals(currentUserName)){
            rowView = View.inflate(mcontext, R.layout.adapter_mensagem_remetente,null);
        } else {
            rowView = View.inflate(mcontext, R.layout.adapter_mensagem_destinatario,null);
        }

        String messageText = message.getText();
        TextView messageField = rowView.findViewById(R.id.textMensagemTexto);
        messageField.setText(messageText);

        TextView usernameField = rowView.findViewById(R.id.username);
        usernameField.setText(messageUserName);

        String messageDate = message.getFormattedDate();
        TextView datetimeField = rowView.findViewById(R.id.datetime);
        datetimeField.setText(messageDate);

        return rowView;
    };

    public void setMessages(ArrayList<Message> messageListDataSet) {
        this.messages = messageListDataSet;
        this.notifyDataSetChanged();
    }

    public void addMessage(Message newMessage){
        this.messages.add(newMessage);
        this.notifyDataSetChanged();
    }
}
