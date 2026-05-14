package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AlertGeneratorTest {

    @Test
    void testBloodPressureThresholdAlert() {
        // 1. Use the Singleton accessor for DataStorage
        DataStorage storage = DataStorage.getInstance();

        // 2. Use the Singleton accessor for AlertGenerator
        AlertGenerator alertGenerator = AlertGenerator.getInstance(storage);

        // Setup a Patient with dangerous blood pressure (Systolic > 180)
        Patient patient = new Patient(1);
        patient.addRecord(190.0, "SystolicBloodPressure", 1000L);

        // 3. FIX: Changed 'generator' to 'alertGenerator' to match the variable name above
        assertDoesNotThrow(() -> alertGenerator.evaluateData(patient));
    }

    @Test
    void testBloodSaturationLowAlert() {
        // 1. Use the Singleton accessor for DataStorage
        DataStorage storage = DataStorage.getInstance();

        // 2. Use the Singleton accessor for AlertGenerator
        AlertGenerator alertGenerator = AlertGenerator.getInstance(storage);
        // Setup Patient with dangerously low oxygen (< 92%)
        Patient patient = new Patient(2);
        patient.addRecord(89.0, "BloodSaturation", 2000L);

        assertDoesNotThrow(() -> alertGenerator.evaluateData(patient));
    }
}