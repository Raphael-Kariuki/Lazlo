package com.example.lazlo;

public class timerModel {
    long startTimerTime;
    long pauseTimerTime;
    long resumeTimerTime;
    long completeTimerTime;
    long lostDuration;
    String lostTimeReason;
    timerModel(long startTimerTime,long pauseTimerTime,long resumeTimerTime,long completeTimerTime, long lostDuration, String lostTimeReason){

        this.startTimerTime = startTimerTime;
        this.pauseTimerTime = pauseTimerTime;
        this.resumeTimerTime = resumeTimerTime;
        this.completeTimerTime = completeTimerTime;
        this.lostDuration = lostDuration;
        this.lostTimeReason = lostTimeReason;
    }

    public void setStartTimerTime(long startTimerTime) {
        this.startTimerTime = startTimerTime;
    }

    public void setPauseTimerTime(long pauseTimerTime) {
        this.pauseTimerTime = pauseTimerTime;
    }

    public void setResumeTimerTime(long resumeTimerTime) {
        this.resumeTimerTime = resumeTimerTime;
    }

    public void setCompleteTimerTime(long completeTimerTime) {
        this.completeTimerTime = completeTimerTime;
    }

    public void setLostDuration(long lostDuration) {
        this.lostDuration = lostDuration;
    }

    public void setLostTimeReason(String lostTimeReason) {
        this.lostTimeReason = lostTimeReason;
    }

    public long getStartTimerTime() {
        return startTimerTime;
    }

    public long getPauseTimerTime() {
        return pauseTimerTime;
    }

    public long getResumeTimerTime() {
        return resumeTimerTime;
    }

    public long getCompleteTimerTime() {
        return completeTimerTime;
    }

    public long getLostDuration() {
        return lostDuration;
    }

    public String getLostTimeReason() {
        return lostTimeReason;
    }
}
