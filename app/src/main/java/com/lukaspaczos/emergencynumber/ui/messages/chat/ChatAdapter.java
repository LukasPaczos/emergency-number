package com.lukaspaczos.emergencynumber.ui.messages.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lukaspaczos.emergencynumber.R;

import java.util.List;

/**
 * Created by Lukas Paczos on 29-Mar-17
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private static final int MESSAGE_TYPE_ME = 0;
    private static final int MESSAGE_TYPE_112 = 1;

    private List<ChatElementModel> dataSet;
    Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case MESSAGE_TYPE_ME:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_message_me, parent, false);
                break;

            case MESSAGE_TYPE_112:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_message_112, parent, false);
                break;
        }

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatElementModel dataModel = dataSet.get(position);
        holder.date.setText(dataModel.getDate());
        holder.isMe = dataModel.isMe();
        holder.message.setText(dataModel.getText());
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).isMe() ? MESSAGE_TYPE_ME : MESSAGE_TYPE_112;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    // View lookup cache
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        boolean isMe;
        TextView message;

        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            message = (TextView) itemView.findViewById(R.id.message);
        }
    }

    public ChatAdapter(List<ChatElementModel> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
    }
}
