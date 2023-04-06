package com.naren008.taskmanager;

public class UserAlarm {
    private int hour;
    private int minute;
    private String task;

    public UserAlarm(){

    }

    public UserAlarm(int hour, int minute, String task) {
        this.hour = hour;
        this.minute = minute;
        this.task = task;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
