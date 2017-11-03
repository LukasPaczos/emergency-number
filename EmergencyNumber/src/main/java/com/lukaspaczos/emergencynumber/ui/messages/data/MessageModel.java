package com.lukaspaczos.emergencynumber.ui.messages.data;

import com.lukaspaczos.emergencynumber.ui.report.Report;

/**
 * Created by Lukas Paczos on 29-Mar-17
 */

public class MessageModel {
    private long timestamp;
    private String sender;
    private String text;
    private Report report;
    private boolean firstMessage;
    private int sent;
    private int delivered;

    public MessageModel(long timestamp, String sender, String text, Report report, boolean firstMessage, int sent, int delivered) {
        this.timestamp = timestamp;
        this.sender = sender;
        this.text = text;
        this.report = report;
        this.firstMessage = firstMessage;
        this.sent = sent;
        this.delivered = delivered;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public Report getReport() {
        return report;
    }

    public boolean isFirstMessage() {
        return firstMessage;
    }

    public int getSent() {
        return sent;
    }

    public int getDelivered() {
        return delivered;
    }
}
