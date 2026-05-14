package com.data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;

/**
 * Client that connects to a WebSocket server to receive real-time health data.
 */
public class MyWebSocketClient extends WebSocketClient {
    private DataStorage storage;

    public MyWebSocketClient(URI serverUri, DataStorage storage) {
        super(serverUri);
        this.storage = storage;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to WebSocket Server");
    }

    @Override
    public void onMessage(String message) {
        try {
            // Format: "PatientID,MeasurementValue,RecordType,Timestamp"
            String[] parts = message.split(",");
            if (parts.length == 4) {
                int patientId = Integer.parseInt(parts[0]);
                // Changed this because when testing % would not show up
                double value = Double.parseDouble(parts[1].replace("%", ""));
                String type = parts[2];
                long timestamp = Long.parseLong(parts[3]);

                // Store in our Singleton DataStorage
                storage.addPatientData(patientId, value, type, timestamp);
                System.out.println("Received and stored: " + message);
            }
        } catch (Exception e) {
            System.err.println("Error parsing real-time message: " + message + "");
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed: " + reason + ". Attempting to reconnect...");
        // Logic to try reconnecting after a few seconds
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                this.reconnect();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket Error: " + ex.getMessage() + "");
    }
}