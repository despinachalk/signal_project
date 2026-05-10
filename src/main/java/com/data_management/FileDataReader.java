package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


 //Reads patient data from files in a specified directory and loads it into DataStorage.

public class FileDataReader implements DataReader {

    private final String directoryPath;

    public FileDataReader(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        File directory = new File(directoryPath);

        // Check if the directory exists
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IOException("Invalid directory path: " + directoryPath);
        }

        // Get all files in the directory
        File[] files = directory.listFiles();
        if (files == null) return;

        // Loop through each file
        for (File file : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null){
                    String[] dataPoints = line.split(",");

                    if (dataPoints.length == 4) {
                        try {
                            int patientId = Integer.parseInt(dataPoints[0].trim());
                            long timestamp = Long.parseLong(dataPoints[1].trim());
                            String label = dataPoints[2].trim();
                            double value = Double.parseDouble(dataPoints[3].trim());

                            // Add the parsed data to DataStorage
                            dataStorage.addPatientData(patientId, value, label, timestamp);
                        } catch (NumberFormatException e) {
                            System.err.println("Skipping malformed line: " + line);
                        }
                    }
                }
            }
        }
    }
}