package com.alerts;

/**
 * Adds prioritization tagging to alerts needing urgent attention.
 */
public class PriorityAlertDecorator extends AlertDecorator {
    public PriorityAlertDecorator(AlertInterface alert) {
        super(alert);
    }

    @Override
    public String getCondition() {
        return "URGENT: " + super.getCondition();
    }
}