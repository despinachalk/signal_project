package com.data_management;

import com.alerts.Alert;
import java.util.ArrayList;
import java.util.List;

/**
  * Strategy to evaluate if a patient's blood pressure shows a critical trend.
  * Triggers if 3 consecutive readings increase or decrease by more than 10 mmHg each.
 */
public class BloodPressureTrendStrategy implements AlertStrategy {

    @Override
    public Alert checkAlert(Patient patient) {
        List<PatientRecord> allRecords = patient.getRecords(0, Long.MAX_VALUE);
        List<PatientRecord> systolicRecords = new ArrayList<>();
        List<PatientRecord> diastolicRecords = new ArrayList<>();

        for (PatientRecord record : allRecords) {
            if (record.getRecordType().equals("SystolicBloodPressure")) {
                systolicRecords.add(record);
            } else if (record.getRecordType().equals("DiastolicBloodPressure")) {
                diastolicRecords.add(record);
            }
        }

        // Check for trends in both lists
        Alert systolicAlert = evaluateTrend(systolicRecords, patient.getPatientId(), "SystolicBloodPressure");
        if (systolicAlert != null) return systolicAlert;

        Alert diastolicAlert = evaluateTrend(diastolicRecords, patient.getPatientId(), "DiastolicBloodPressure");
        if (diastolicAlert != null) return diastolicAlert;

        return null;
    }

    private Alert evaluateTrend(List<PatientRecord> records, int patientId, String recordType) {
        // Base case needs atleast 3 records
        if (records.size() < 3) return null;

        // Sliding window of size 3
        for (int i = 0; i < records.size() - 2; i++) {
            double val1 = records.get(i).getMeasurementValue();
            double val2 = records.get(i + 1).getMeasurementValue();
            double val3 = records.get(i + 2).getMeasurementValue();

            // Check if it's consistently increasing by MORE than 10 each time
            boolean isIncreasing = (val2 - val1 > 10) && (val3 - val2 > 10);

            // Check if it's consistently decreasing by MORE than 10 each time
            boolean isDecreasing = (val1 - val2 > 10) && (val2 - val3 > 10);

            if (isIncreasing || isDecreasing) {
                String trendType = isIncreasing ? "Increasing" : "Decreasing";
                return new Alert(
                        Integer.toString(patientId),
                        trendType + " trend detected in " + recordType,
                        records.get(i + 2).getTimestamp() // Trigger time is the 3rd reading
                );
            }
        }

        return null;
    }
}