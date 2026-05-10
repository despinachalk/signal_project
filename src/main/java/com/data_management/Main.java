package com.data_management;

import com.cardio_generator.HealthDataSimulator;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Check if a command-line argument was provided
        if (args.length > 0 && args[0].equals("DataStorage")) {
            // If the argument is "DataStorage", run the DataStorage system
            System.out.println("Starting DataStorage System...");
            DataStorage.main(new String[]{});
        } else {
            // Otherwise, default to running the original simulator
            System.out.println("Starting HealthDataSimulator...");
            HealthDataSimulator.main(args);
        }
    }
}
