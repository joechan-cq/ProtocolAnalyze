package com.joe.protocolanalyze;

/**
 * Description
 * Created by chenqiao on 2015/8/28.
 */
public class Info {

    private int id;

    private Task task;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Info(int id, Task task) {
        this.id = id;
        this.task = task;
    }

    public Info() {
    }
}
