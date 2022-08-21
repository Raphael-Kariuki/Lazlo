package com.example.lazlo;

public class taskModel {

    private String taskTitle,taskDescription, taskAssociatedPrice,
            taskCategory,taskCreationTime,taskDeadline,taskPredictedDuration, taskState , parentTaskId;
    private Double randTaskId,randUserId;

    //constructor
    taskModel(String taskTitle, String taskDescription, String taskAssociatedPrice,String taskCategory,String taskCreationTime
    ,String taskDeadline,String taskPredictedDuration,String taskState,String parentTaskId){
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskAssociatedPrice = taskAssociatedPrice;
        this.taskCategory = taskCategory;
        this.taskCreationTime = taskCreationTime;
        this.taskDeadline =taskDeadline;
        this.taskPredictedDuration = taskPredictedDuration;
        this.taskState = taskState;
        this.parentTaskId = parentTaskId;
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

    public String getTaskCategory() {
        return taskCategory;
    }

    public Double getRandTaskId() {
        return randTaskId;
    }

    public Double getRandUserId() {
        return randUserId;
    }

    public String getParentTaskId() {
        return parentTaskId;
    }

    public String getTaskCreationTime() {
        return taskCreationTime;
    }

    public String getTaskPredictedDuration() {
        return taskPredictedDuration;
    }

    public String getTaskDeadline() {
        return taskDeadline;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }

    public void setRandTaskId(Double randTaskId) {
        this.randTaskId = randTaskId;
    }

    public void setRandUserId(Double randUserId) {
        this.randUserId = randUserId;
    }

    public void setTaskCreationTime(String taskCreationTime) {
        this.taskCreationTime = taskCreationTime;
    }

    public void setTaskDeadline(String taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public void setTaskPredictedDuration(String taskPredictedDuration) {
        this.taskPredictedDuration = taskPredictedDuration;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
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
