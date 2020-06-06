package com.example.tellmamobile;

import android.app.Activity;
import android.util.Log;
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
        Log.d("my-id", UserSession.getInstance().getId());
        Log.d("message-id", messages.get(position).getIdUser());
        if (messages.get(position).getIdUser().compareTo(UserSession.getInstance().getId()) == 0 ){
            rowView=inflater.inflate(R.layout.adapter_mensagem_remetente, null,true);
        }else{
            rowView=inflater.inflate(R.layout.adapter_mensagem_destinatario, null,true);
        }

        TextView message = rowView.findViewById(R.id.textMensagemTexto);

        message.setText(messages.get(position).getText());
        return rowView;
    };
}
