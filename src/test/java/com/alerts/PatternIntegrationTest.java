package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.BloodPressureStrategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PatternIntegrationTest {

    @Test
    void testSingletonDataStorage() {
        // Verify that only one instance of DataStorage exists
        DataStorage s1 = DataStorage.getInstance();
        DataStorage s2 = DataStorage.getInstance();
        assertSame(s1, s2, "Both instances should be the same");
    }

    @Test
    void testFactoryMethodCreation() {
        // Verify specific alert factory creates the correct type
        AlertFactory factory = new BloodPressureAlertFactory();
        AlertInterface alert = factory.createAlert("1", "High BP", 1000L);

        assertTrue(alert instanceof BloodPressureAlert, "Factory should produce BloodPressureAlert instances");
        assertEquals("High BP", alert.getCondition());
    }

    @Test
    void testPriorityDecorator() {
        // Verify the PriorityAlertDecorator modifies the condition string
        AlertInterface basicAlert = new Alert("1", "Critical Issue", 1000L);
        AlertInterface priorityAlert = new PriorityAlertDecorator(basicAlert);

        assertTrue(priorityAlert.getCondition().startsWith("URGENT:"),
                "Priority decorator should prepend 'URGENT:' to the condition");
    }

    @Test
    void testRepeatedDecorator() {
        // Verify the RepeatedAlertDecorator appends the repeated tag
        AlertInterface basicAlert = new Alert("1", "Low Oxygen", 1000L);
        AlertInterface repeatedAlert = new RepeatedAlertDecorator(basicAlert);

        assertTrue(repeatedAlert.getCondition().contains("[REPEATED]"),
                "Repeated decorator should add the [REPEATED] tag");
    }

    @Test
    void testStrategyLogicIntegration() {
        // Verify strategy uses factory to trigger alert
        Patient patient = new Patient(10);
        patient.addRecord(200.0, "SystolicBloodPressure", 5000L); // Critical high

        BloodPressureStrategy strategy = new BloodPressureStrategy();
        AlertInterface result = strategy.checkAlert(patient);

        assertNotNull(result, "Strategy should return an alert for critical BP");
        assertTrue(result.getCondition().contains("Critical"), "Alert condition should be correctly set");
    }
}