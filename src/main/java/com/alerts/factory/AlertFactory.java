package com.alerts.factory;

import com.alerts.Alert;

/**
 * Abstract class for creating alerts
 */
public abstract class AlertFactory {
    public abstract Alert createAlert(int patientId, String condition, long timestamp);
}