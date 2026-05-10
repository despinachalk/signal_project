package com.data_management;

import com.alerts.Alert;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Strategy to evaluate abnormal ECG peaks using a sliding window average.
 */
public class EcgAlertStrategy implements AlertStrategy {

    @Override
    public Alert checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);

        // Use a Queue to act as our sliding window
        Queue<Double> window = new LinkedList<>();
        int windowSize = 5;
        double sum = 0;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("ECG")) {
                double value = record.getMeasurementValue();
                // Add the new value to our window and sum
                window.offer(value);
                sum += value;
                // If the window gets too big, remove the oldest value
                if (window.size() > windowSize) {
                    sum -= window.poll();
                }
                // Only evaluate once the window is actually full
                if (window.size() == windowSize) {
                    double average = sum / windowSize;
                    if (value > (average * 1.5)) {
                        return new Alert(
                                Integer.toString(patient.getPatientId()),
                                "Abnormal ECG Peak: " + value + " (Avg: " + average + ")",
                                record.getTimestamp()
                        );
                    }
                }
            }
        }
        return null;
    }
}