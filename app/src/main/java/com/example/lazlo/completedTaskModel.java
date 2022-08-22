package com.example.lazlo;

import java.util.Comparator;

public class completedTaskModel {


    private String taskTitle,taskDescription,taskCategory,taskAssociatedPrice,taskDeadline,taskPredictedDuration,ParentTaskId;
    private long taskCreationTime,taskStartTime,taskCompleteTime,taskDuration;
    private Double randTaskId,randUserId;
    private int taskTrial;
    private int taskState;
    private String parentTaskId;

    completedTaskModel(Double randTaskId, Double randUserId, String taskTitle, String taskDescription,
                       String taskAssociatedPrice, String taskCategory ,String taskDeadline, long taskCreationTime, long taskStartTime,
                       long taskCompleteTime, long taskDuration
            , String taskPredictedDuration, int taskTrial, int taskState, String parentTaskId){
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
        this.parentTaskId = parentTaskId;
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

    public String getParentTaskId() {
        return ParentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        ParentTaskId = parentTaskId;
    }

    public  static Comparator<completedTaskModel> tasksDeadlineComparatorAsc = (t1, t2) -> {
        String taskDeadline1 = t1.getTaskDeadline();
        String taskDeadline2 = t2.getTaskDeadline();

        //ascending order
        return taskDeadline1.compareTo(taskDeadline2);
    };
    public  static Comparator<completedTaskModel> tasksDeadlineComparatorDesc = (t1, t2) -> {
        String taskDeadline1 = t1.getTaskDeadline();
        String taskDeadline2 = t2.getTaskDeadline();

        //descending order
        return taskDeadline2.compareTo(taskDeadline1);
    };
    public  static Comparator<completedTaskModel> tasksPriceComparatorDesc = (t1, t2) -> {
        String taskPrice1 = t1.getTaskAssociatedPrice();
        String taskPrice2 = t2.getTaskAssociatedPrice();

        //ascending order
        return taskPrice1.compareTo(taskPrice2);
    };
    public  static Comparator<completedTaskModel> tasksPriceComparatorAsc = (t1, t2) -> {
        String taskPrice1 = t1.getTaskDeadline();
        String taskPrice2 = t2.getTaskDeadline();

        //descending order
        return taskPrice2.compareTo(taskPrice1);
    };

    public  static Comparator<completedTaskModel> tasksCreationComparatorDesc = (t1, t2) -> {
        long tasksCreation1 = t1.getTaskCreationTime();
        long tasksCreation2 = t2.getTaskCreationTime();

        //ascending order
        return String.valueOf(tasksCreation1).compareTo(String.valueOf(tasksCreation2));
    };
    public  static Comparator<completedTaskModel> tasksCreationComparatorAsc = (t1, t2) -> {
        long tasksCreation1 = t1.getTaskCreationTime();
        long tasksCreation2 = t2.getTaskCreationTime();

        //descending order
        return String.valueOf(tasksCreation2).compareTo(String.valueOf(tasksCreation1));
    };
    public  static Comparator<completedTaskModel> tasksDurationComparatorDesc = (t1, t2) -> {
        String tasksDuration1 = t1.getTaskPredictedDuration();
        String tasksDuration2 = t2.getTaskPredictedDuration();

        //ascending order
        return tasksDuration1.compareTo(tasksDuration2);
    };
    public  static Comparator<completedTaskModel> tasksDurationComparatorAsc = (t1, t2) -> {
        String tasksDuration1 = t1.getTaskPredictedDuration();
        String tasksDuration2 = t2.getTaskPredictedDuration();

        //descending order
        return tasksDuration2.compareTo(tasksDuration1);
    };
}
