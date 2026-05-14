package com.data_management;

import com.alerts.AlertGenerator;
import java.io.IOException;

/**
 * Main entry point for the real-time monitoring system.
 */
public class RealTimeSystemMain {
    public static void main(String[] args) {
        // Initialize  Storage
        DataStorage storage = DataStorage.getInstance();

        // Initialize the AlertGenerator
        AlertGenerator.getInstance(storage);

        // Define the WebSocket server URL
        String serverUrl = "ws://localhost:8080";

        // Create and start the WebSocket DataReader
        WebSocketDataReader reader = new WebSocketDataReader(serverUrl);

        try {
            System.out.println("Starting Real-Time Monitoring System...");
            System.out.println("Connecting to stream at: " + serverUrl);

            // This starts the WebSocketClient in a separate thread
            reader.readData(storage);

            // Keep the main thread alive to listen for incoming data
            while (true) {
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Critical System Error: " + e.getMessage() + "");
        }
    }
}