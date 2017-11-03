package com.lukaspaczos.emergencynumber.ui.history.list;

import com.lukaspaczos.emergencynumber.ui.report.Report;

import java.util.List;

/**
 * Created by Lukas Paczos on 06-Apr-17
 */

public class HistoryEntryModel {
    private long timestamp;
    private List<String> categories;
    private String location;
    private Report report;

    public HistoryEntryModel(long timestamp, List<String> categories, String location, Report report) {
        this.timestamp = timestamp;
        this.categories = categories;
        this.location = location;
        this.report = report;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}
