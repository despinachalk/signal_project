package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
/*
strategy implementation for outputting patient data over a TCP network connection.
the class sets up a TCP server on a specified port and sends formatted patient data
to the connected client.
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    /*
    constructs a TcpOutputStrategy and initializes a TCP server on the given port.
    the server listens for a single client connection in a separate thread so it doesn't
    block the main application thread.

     @param port the network port number on which the TCP server will listen for connections
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
     sends generated patient data to the connected TCP client.
     data is formatted as a comma-separated string containing the patient ID,timestamp, label, and data.

     @param patientId unique identifier of the patient
     @param timestamp the time the data was generated, in milliseconds since the UNIX epoch
     @param label     a string representing the category of the data
     @param data      the actual data value or message
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
