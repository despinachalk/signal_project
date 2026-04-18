package com.cardio_generator.outputs;

public interface OutputStrategy {
    /*
    @param patientId unique patient id identifier
    @param timestamp the time data was generated in milliseconds
    @param label a string representing the category of data
    @param data the actual data value or message
     */
    void output(int patientId, long timestamp, String label, String data);
}
