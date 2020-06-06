package com.example.tellmamobile;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {

    private final Activity context;

    private final ArrayList<Message> messages;

    public MessageAdapter(Activity context, ArrayList<Message> messages) {
        super(context,0, messages);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.messages = messages;

    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();

        View rowView;
        String messageUserName = messages.get(position).getUsername();
        String currentUserName = UserSession.getInstance().getUsername();

        if (messageUserName.equals(currentUserName)){
            rowView=inflater.inflate(R.layout.adapter_mensagem_remetente, null,true);
        }else{
            rowView=inflater.inflate(R.layout.adapter_mensagem_destinatario, null,true);
        }

        TextView message = rowView.findViewById(R.id.textMensagemTexto);

        message.setText(messages.get(position).getText());
        return rowView;
    };
}
