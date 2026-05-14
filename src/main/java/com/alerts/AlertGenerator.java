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
 * and generating alerts when certain predefined conditions are met.
 * This class follows the Singleton pattern.
 */
public class AlertGenerator {
    private static AlertGenerator instance; // Singleton instance
    private DataStorage dataStorage;
    private List<AlertStrategy> alertStrategies;

    /**
     * Private constructor to prevent instantiation from other classes.
     *
     * @param dataStorage the data storage system that provides access to patient data
     */
    private AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.alertStrategies = new ArrayList<>();

        // Concrete strategies required by the rubric [cite: 27]
        this.alertStrategies.add(new BloodPressureStrategy());
        this.alertStrategies.add(new HeartRateStrategy());
        this.alertStrategies.add(new OxygenSaturationStrategy());

        // Specialized strategies
        this.alertStrategies.add(new ManualAlertStrategy());
        this.alertStrategies.add(new HypotensiveHypoxemiaStrategy());
    }

    /**
     * Static method to provide a global point of access to the AlertGenerator.
     *  @param storage The DataStorage instance.
     * @return The single instance of AlertGenerator.
     */
    public static synchronized AlertGenerator getInstance(DataStorage storage) {
        if (instance == null) {
            instance = new AlertGenerator(storage);
        }
        return instance;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions are met.
     * This now supports real-time evaluation as data arrives[cite: 57, 86].
     *
     * @param patient the patient data to evaluate
     */
    public void evaluateData(Patient patient) {
        for (AlertStrategy strategy : alertStrategies) {
            // Uses AlertInterface to support Decorators [cite: 37]
            AlertInterface potentialAlert = strategy.checkAlert(patient);

            if (potentialAlert != null) {
                // Apply Decorators dynamically for critical conditions [cite: 35]
                if (potentialAlert.getCondition().toLowerCase().contains("critical")) {
                    potentialAlert = new PriorityAlertDecorator(potentialAlert); // [cite: 41]
                }

                triggerAlert(potentialAlert);
            }
        }
    }

    /**
     * Triggers an alert for the monitoring system.
     *
     * @param alert the alert object containing details
     */
    private void triggerAlert(AlertInterface alert) { // Updated to AlertInterface [cite: 37]
        System.out.println("ALERT TRIGGERED: Patient " + alert.getPatientId()
                + " - Condition: " + alert.getCondition()
                + " at time " + alert.getTimestamp());
    }
}