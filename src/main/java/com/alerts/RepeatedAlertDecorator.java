package com.alerts;

/**
 * Adds functionality to indicate an alert is being repeated.
 */
public class RepeatedAlertDecorator extends AlertDecorator {
    public RepeatedAlertDecorator(AlertInterface alert) {
        super(alert);
    }

    @Override
    public String getCondition() {
        return super.getCondition() + " [REPEATED]";
    }
}