package com.example.tellmamobile;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {
    private String[] chatListDataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView cardView;
        public MyViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public ChatListAdapter(String[] myChatListDataSet) {
        chatListDataSet = myChatListDataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_chat_list, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Button buttonView = (Button) holder.cardView.findViewById(R.id.chat_button);
        buttonView.setText(chatListDataSet[position]);

    }

    @Override
    public int getItemCount() {
        return chatListDataSet.length;
    }
}

