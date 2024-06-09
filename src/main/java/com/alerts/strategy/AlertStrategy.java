package com.alerts.strategy;

/**
 * Interface for the alert strategy
 */
public interface AlertStrategy {
    /**
     * Checks the alert for a specific given patient
     * @param patientId the id of the patient
     */
    public void checkAlert(int patientId);
}
