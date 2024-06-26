package com.alerts.decorator;

import com.alerts.Alert;

/**
 * PriorityAlertDecorator class
 */
public class PriorityAlertDecorator extends AlertDecorator {

    public PriorityAlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert);
    }

    /**
     * Trigger alert
     */
    @Override
    public void triggerAlert(){
        super.triggerAlert();
        System.out.println("Priority alert triggered");
    }
}