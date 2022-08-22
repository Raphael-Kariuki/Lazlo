package com.example.lazlo;

import java.util.Comparator;

public class taskModel {

    private String taskTitle,taskDescription, taskAssociatedPrice,
            taskCategory,taskCreationTime,taskDeadline,taskPredictedDuration, taskState , parentTaskId;
    private Double randTaskId,randUserId;

    //constructor
    taskModel(Double randTaskId,Double randUserId,String taskTitle, String taskDescription, String taskAssociatedPrice,String taskCategory,String taskCreationTime
    ,String taskDeadline,String taskPredictedDuration,String taskState,String parentTaskId){
        this.randTaskId = randTaskId;
        this.randUserId = randUserId;
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
    public  static Comparator<taskModel> tasksDeadlineComparatorAsc = (t1, t2) -> {
        String taskDeadline1 = t1.getTaskDeadline();
        String taskDeadline2 = t2.getTaskDeadline();

        //ascending order
        return taskDeadline1.compareTo(taskDeadline2);
    };
    public  static Comparator<taskModel> tasksDeadlineComparatorDesc = (t1, t2) -> {
        String taskDeadline1 = t1.getTaskDeadline();
        String taskDeadline2 = t2.getTaskDeadline();

        //descending order
        return taskDeadline2.compareTo(taskDeadline1);
    };
    public  static Comparator<taskModel> tasksPriceComparatorDesc = (t1, t2) -> {
        String taskPrice1 = t1.getTaskAssociatedPrice();
        String taskPrice2 = t2.getTaskAssociatedPrice();

        //ascending order
        return taskPrice1.compareTo(taskPrice2);
    };
    public  static Comparator<taskModel> tasksPriceComparatorAsc = (t1, t2) -> {
        String taskPrice1 = t1.getTaskDeadline();
        String taskPrice2 = t2.getTaskDeadline();

        //descending order
        return taskPrice2.compareTo(taskPrice1);
    };

    public  static Comparator<taskModel> tasksCreationComparatorDesc = (t1, t2) -> {
        String tasksCreation1 = t1.getTaskCreationTime();
        String tasksCreation2 = t2.getTaskCreationTime();

        //ascending order
        return tasksCreation1.compareTo(tasksCreation2);
    };
    public  static Comparator<taskModel> tasksCreationComparatorAsc = (t1, t2) -> {
        String tasksCreation1 = t1.getTaskCreationTime();
        String tasksCreation2 = t2.getTaskCreationTime();

        //descending order
        return tasksCreation2.compareTo(tasksCreation1);
    };
    public  static Comparator<taskModel> tasksDurationComparatorDesc = (t1, t2) -> {
        String tasksDuration1 = t1.getTaskPredictedDuration();
        String tasksDuration2 = t2.getTaskPredictedDuration();

        //ascending order
        return tasksDuration1.compareTo(tasksDuration2);
    };
    public  static Comparator<taskModel> tasksDurationComparatorAsc = (t1, t2) -> {
        String tasksDuration1 = t1.getTaskPredictedDuration();
        String tasksDuration2 = t2.getTaskPredictedDuration();

        //descending order
        return tasksDuration2.compareTo(tasksDuration1);
    };
}
