package com.alerts;

/**
 * This class represents an alert that is triggered when a patient's health condition is critical.
 */
public class Alert {
    private int patientId;
    private String condition;
    private long timestamp;

    public Alert(int patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getCondition() {
        return condition;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void triggerAlert(){
        System.out.println("Alert " + condition + " for patient " + patientId + " at " + timestamp);    }
}
