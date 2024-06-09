package com.alerts.decorator;

import com.alerts.Alert;

/**
 * RepeatedAlertDecorator class
 */
public class RepeatedAlertDecorator extends AlertDecorator{
    public RepeatedAlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert);
    }

    /**
     * Trigger alert
     */
    @Override
    public void triggerAlert(){
        super.triggerAlert();
        System.out.println("Repeated alert check");
    }
}