package com.lukaspaczos.emergencynumber.ui.messages.chat;

import com.lukaspaczos.emergencynumber.ui.messages.data.MessageModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Lukas Paczos on 29-Mar-17
 */

public class ChatElementModel {
    private Date date;
    private String text;
    private boolean isMe;
    private boolean isSent;
    private boolean isDelivered;

    public ChatElementModel(Date date, String text, boolean isMe) {
        this.date = date;
        this.text = text;
        this.isMe = isMe;
    }

    public ChatElementModel(Date date, String text, boolean isMe, boolean isSent, boolean isDelivered) {
        this.date = date;
        this.text = text;
        this.isMe = isMe;
        this.isSent = isSent;
        this.isDelivered = isDelivered;
    }

    public String getDate() {
        SimpleDateFormat sdm = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault());
        return sdm.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public static ChatElementModel fromMessageModel(MessageModel model) {
        return new ChatElementModel(
                new Date(model.getTimestamp()),
                model.getText(),
                model.getSender().equals("me"),
                model.getSent() == 1,
                model.getDelivered() == 1
        );
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ChatElementModel
                && (this.isMe() == ((ChatElementModel) obj).isMe())
                && this.getText().equals(((ChatElementModel) obj).getText())
                && this.getDate().equals(((ChatElementModel) obj).getDate());
    }
}
