package com.zubisoft.campushelpdeskstudent.models;

import java.io.Serializable;

public class Request implements Serializable {

    private String id;
    private String title;
    private String body;
    private String userId;
    private String status;
    private String moderatorId;
    private long timestamp;

    public Request() {
    }

    public Request(String title, String body, String userId, String status, String moderatorId, long timestamp) {
        this.title = title;
        this.body = body;
        this.userId = userId;
        this.status = status;
        this.moderatorId = moderatorId;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(String moderatorId) {
        this.moderatorId = moderatorId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
