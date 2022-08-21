package com.example.lazlo;

public class taskModel {
    private String taskTitle;
    private String taskDescription;
    private String taskAssociatedPrice;

    //constructor
    taskModel(String taskTitle, String taskDescription, String taskAssociatedPrice){
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskAssociatedPrice = taskAssociatedPrice;
    }
    //getters and setters


    public String getTaskTitle() {
        return taskTitle;
    }

    public String getTaskAssociatedPrice() {
        return taskAssociatedPrice;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setTaskAssociatedPrice(String taskAssociatedPrice) {
        this.taskAssociatedPrice = taskAssociatedPrice;
    }
}
