package com.example.lazlo;

public class completedTaskModel {


    private String taskTitle,taskDescription,taskCategory,taskAssociatedPrice,taskDeadline,taskPredictedDuration;
    private long taskCreationTime,taskStartTime,taskCompleteTime,taskDuration;
    private Double randTaskId,randUserId;
    private int taskTrial, taskState;

    completedTaskModel(Double randTaskId, Double randUserId, String taskTitle, String taskDescription,
                       String taskAssociatedPrice, String taskCategory ,String taskDeadline, long taskCreationTime, long taskStartTime,
                       long taskCompleteTime, long taskDuration
            , String taskPredictedDuration, int taskTrial, int taskState){
        this.randTaskId = randTaskId;
        this.randUserId = randUserId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskAssociatedPrice = taskAssociatedPrice;
        this.taskCategory = taskCategory;
        this.taskCreationTime = taskCreationTime;
        this.taskStartTime = taskStartTime;
        this.taskCompleteTime = taskCompleteTime;
        this.taskDuration = taskDuration;
        this.taskDeadline = taskDeadline;
        this.taskPredictedDuration = taskPredictedDuration;
        this.taskTrial = taskTrial;
        this.taskState = taskState;
    }
    //getters and setters

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }

    public void setRandUserId(Double randUserId) {
        this.randUserId = randUserId;
    }

    public void setRandTaskId(Double randTaskId) {
        this.randTaskId = randTaskId;
    }

    public void setTaskDeadline(String taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public void setTaskPredictedDuration(String taskPredictedDuration) {
        this.taskPredictedDuration = taskPredictedDuration;
    }

    public void setTaskCreationTime(long taskCreationTime) {
        this.taskCreationTime = taskCreationTime;
    }

    public void setTaskAssociatedPrice(String taskAssociatedPrice) {
        this.taskAssociatedPrice = taskAssociatedPrice;
    }

    public void setTaskCompleteTime(long taskCompleteTime) {
        this.taskCompleteTime = taskCompleteTime;
    }

    public void setTaskDuration(long taskDuration) {
        this.taskDuration = taskDuration;
    }

    public void setTaskStartTime(long taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public void setTaskTrial(int taskTrial) {
        this.taskTrial = taskTrial;
    }

    public String getTaskDeadline() {
        return taskDeadline;
    }

    public String getTaskPredictedDuration() {
        return taskPredictedDuration;
    }

    public Double getRandUserId() {
        return randUserId;
    }

    public Double getRandTaskId() {
        return randTaskId;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getTaskAssociatedPrice() {
        return taskAssociatedPrice;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public int getTaskState() {
        return taskState;
    }

    public int getTaskTrial() {
        return taskTrial;
    }

    public long getTaskCompleteTime() {
        return taskCompleteTime;
    }

    public long getTaskCreationTime() {
        return taskCreationTime;
    }

    public long getTaskDuration() {
        return taskDuration;
    }

    public long getTaskStartTime() {
        return taskStartTime;
    }
}
