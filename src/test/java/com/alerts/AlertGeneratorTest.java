package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AlertGeneratorTest {

    @Test
    void testBloodPressureThresholdAlert() {
        // Setup DataStorage and AlertGenerator
        DataStorage storage = new DataStorage();
        AlertGenerator generator = new AlertGenerator(storage);

        // Setup a Patient with dangerous blood pressure (Systolic > 180)
        Patient patient = new Patient(1);
        patient.addRecord(190.0, "SystolicBloodPressure", 1000L);

        // Evaluate the patient
        assertDoesNotThrow(() -> generator.evaluateData(patient),
                "Evaluating data should not throw any exceptions.");
    }

    @Test
    void testBloodSaturationLowAlert() {
        DataStorage storage = new DataStorage();
        AlertGenerator generator = new AlertGenerator(storage);

        // Setup Patient with dangerously low oxygen (< 92%)
        Patient patient = new Patient(2);
        patient.addRecord(89.0, "BloodSaturation", 2000L);

        assertDoesNotThrow(() -> generator.evaluateData(patient));
    }
}