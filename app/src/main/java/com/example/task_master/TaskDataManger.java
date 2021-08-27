package com.example.task_master;

import java.util.ArrayList;
import java.util.List;

public class TaskDataManger {

    private static TaskDataManger instance = null;
    private List<StatusItems> taskItems = new ArrayList<>();

    private TaskDataManger() {
    }

    public static TaskDataManger getInstance() {
        if (instance == null) {
            instance = new TaskDataManger();
        }

        return instance;
    }

    public void setData(List<StatusItems> data) {
        taskItems = data;
    }

    public List<StatusItems> getData() {
        return taskItems;
    }
}
