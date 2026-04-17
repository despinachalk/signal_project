package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

public class AlertGenerator implements PatientDataGenerator {
// Changed variable to upper case to follow standards
    public static final Random RANDOM_GENERATOR = new Random();
    // furthermore changed this to lower case as its local variable
    private boolean[] alertStates; // false = resolved, true = pressed

   /*
   first constructs AlertGenerator and puts every patient to false so its resolved
   @param patientCount being the total num of patients being monitored
    */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }
    /*
    generates and outputs alerts for patients
    determines whether to create new alert or resovle an existing one
    sends result through provided strategy
    @param patientId unique id for each patient
    @param  outputStrategy  strategy used to output the data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
