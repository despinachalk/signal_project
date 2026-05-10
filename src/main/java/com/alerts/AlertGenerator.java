package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.AlertStrategy;
import com.data_management.BloodPressureStrategy;
import com.data_management.HeartRateStrategy;
import com.data_management.OxygenSaturationStrategy;
import com.data_management.ManualAlertStrategy;
import com.data_management.HypotensiveHypoxemiaStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private List<AlertStrategy> alertStrategies; // Holds our alert strategies

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     *
     * @param dataStorage the data storage system that provides access to patient data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;

        // Initialize the strategies list and register our strategies
        this.alertStrategies = new ArrayList<>();

        // Updated to include the concrete strategies
        this.alertStrategies.add(new BloodPressureStrategy());
        this.alertStrategies.add(new HeartRateStrategy());
        this.alertStrategies.add(new OxygenSaturationStrategy());

        // Keeping your existing specialized strategies
        this.alertStrategies.add(new ManualAlertStrategy());
        this.alertStrategies.add(new HypotensiveHypoxemiaStrategy());
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        // Loop through all registered strategies and check for alerts
        for (AlertStrategy strategy : alertStrategies) {
            // Updated to AlertInterface to support the Decorator pattern
            AlertInterface potentialAlert = strategy.checkAlert(patient);

            if (potentialAlert != null) {
                // Logic to apply Decorators dynamically based on conditions
                if (potentialAlert.getCondition().toLowerCase().contains("critical") ||
                        potentialAlert.getCondition().toLowerCase().contains("urgent")) {

                    // Wrap the alert in a Priority decorator
                    potentialAlert = new PriorityAlertDecorator(potentialAlert);
                }

                triggerAlert(potentialAlert);
            }
        }
    }

    /**
     * Triggers an alert for the monitoring system.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(AlertInterface alert) { // Updated parameter to AlertInterface
        System.out.println("ALERT TRIGGERED: Patient " + alert.getPatientId()
                + " - Condition: " + alert.getCondition()
                + " at time " + alert.getTimestamp());
    }
}