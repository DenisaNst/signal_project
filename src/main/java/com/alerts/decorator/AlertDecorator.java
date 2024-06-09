package com.alerts.decorator;

import com.alerts.Alert;

/**
 * Abstract class for the decorator pattern
 */
public class AlertDecorator extends Alert {
    protected Alert decoratedAlert;

    /**
     * Constructor for the AlertDecorator class
     * @param decoratedAlert
     */
    public AlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert.getPatientId(), decoratedAlert.getCondition(), decoratedAlert.getTimestamp());
        this.decoratedAlert = decoratedAlert;
    }

    /**
     * Method to trigger the alert
     */
    public void triggerAlert(){
        decoratedAlert.triggerAlert();

    }
}