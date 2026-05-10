package com.alerts;

/**
 * Base class for creating alerts using the Factory Method pattern.
 */
public abstract class AlertFactory {
    /**
     * Factory method to be implemented by subclasses.
     */
    public abstract AlertInterface createAlert(String patientId, String condition, long timestamp);
}