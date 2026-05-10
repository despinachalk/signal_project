package com.alerts;


// Interface for alerts to support the Decorator pattern.

public interface AlertInterface {
    String getPatientId();
    String getCondition();
    long getTimestamp();
}
