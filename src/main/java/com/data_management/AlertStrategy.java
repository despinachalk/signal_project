package com.data_management;
import com.alerts.Alert;
/**
 * Interface defining the contract for different types of health alerts.
 * Implements the Strategy Pattern to allow new alert types to be added
 * without modifying the core AlertGenerator class .
 */
public interface AlertStrategy {
    /**
     * Checks a patient's records to determine if a specific alert condition is met.
     * @param patient The patient whose data is being evaluated.
     * @return An Alert object if the condition is met, or null if it is not.
     */
    Alert checkAlert(Patient patient);
}