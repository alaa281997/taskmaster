package com.example.task_master;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class StatusItems {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "task_name")
    private String title;

    @ColumnInfo(name = "body")
    private String body;

    @ColumnInfo(name = "status_name")
    private String status;

    public StatusItems(String title, String body, String status) {
        this.title = title;
        this.body = body;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getStatus() {
        return status;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
