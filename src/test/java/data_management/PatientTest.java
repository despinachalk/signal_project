package data_management;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    @Test
    void testAddAndGetRecords() {
        // Create a new patient
        Patient patient = new Patient(1);

        // Add records at different timestamps
        patient.addRecord(120.0, "HeartRate", 100L);
        patient.addRecord(130.0, "HeartRate", 200L);
        patient.addRecord(140.0, "HeartRate", 300L);

        // Retrieve records between 150 and 250
        List<PatientRecord> records = patient.getRecords(150L, 250L);

        // Expect exactly 1 record (the one at timestamp 200)
        assertEquals(1, records.size(), "Should only retrieve one record within the time range");
        assertEquals(130.0, records.get(0).getMeasurementValue(), "The retrieved record should have the value 130.0");
    }

    @Test
    void testGetRecordsEdgeCases() {
        Patient patient = new Patient(2);

        patient.addRecord(90.0, "SystolicBloodPressure", 100L);
        patient.addRecord(120.0, "SystolicBloodPressure", 200L);

        // Test exactly on the boundary (inclusive)
        List<PatientRecord> exactBounds = patient.getRecords(100L, 200L);
        assertEquals(2, exactBounds.size(), "Should include records exactly on the start and end times");
    }

    @Test
    void testGetRecordsOutsideRange() {
        Patient patient = new Patient(3);

        patient.addRecord(98.6, "Temperature", 500L);

        // Query a time range before the record
        List<PatientRecord> beforeRecords = patient.getRecords(100L, 400L);
        assertTrue(beforeRecords.isEmpty(), "Should return an empty list if no records are in range");
    }
}