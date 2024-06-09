package com.alerts.factory;

import com.alerts.Alert;

/**
 * Abstract class for creating alerts
 */
public class BloodOxygenAlertFactory extends AlertFactory{

    @Override
    public Alert createAlert(int patientId, String condition, long timestamp) {
        return new Alert(patientId, condition, timestamp);
    }
}