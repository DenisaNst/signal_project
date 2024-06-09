package com.data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;

/**
 * Reads real-time data from a WebSocket server and stores it in a DataStorage object.
 */
public class RealTimeDataReader implements DataReader, DataReader2 {

    private WebSocketClient client;

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        throw new UnsupportedOperationException("Use readRealTimeData for real-time data processing.");
    }

    /**
     * Reads data from a WebSocket server and stores it in a DataStorage object.
     *
     * @param uri         the URI of the WebSocket server
     * @param dataStorage the DataStorage object to store the data
     * @throws URISyntaxException if the URI is invalid
     * @throws InterruptedException if the connection is interrupted
     */
    @Override
    public void readRealTimeData(String uri, DataStorage dataStorage) throws URISyntaxException, InterruptedException {
        client = new WebSocketClient(new URI(uri)) {

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("Connected to WebSocket server");
            }

            @Override
            public void onMessage(String message) {
                System.out.println("Received data: " + message);
                parseAndStoreMessage(message, dataStorage);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("Connection closed with exit code " + code + " additional info: " + reason);
            }

            @Override
            public void onError(Exception ex) {
                System.err.println("An error occurred: " + ex.getMessage());
                ex.printStackTrace();
            }

            /**
             * Parses the message and stores the data in the DataStorage object.
             *
             * @param message     the message received from the WebSocket server
             * @param dataStorage the DataStorage object to store the data
             */
            private void parseAndStoreMessage(String message, DataStorage dataStorage) {
                // Assuming the message format is: "patientId,timestamp,label,data"
                String[] parts = message.split(",");
                if (parts.length == 4) {
                    try {
                        int patientId = Integer.parseInt(parts[0]);
                        long timestamp = Long.parseLong(parts[1]);
                        String label = parts[2];
                        double data = Double.parseDouble(parts[3]);
                        dataStorage.addPatientData(patientId, timestamp, label, data);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing message: " + message);
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Invalid message format: " + message);
                }
            }
        };

        client.connectBlocking(); // Blocks until the connection is established
    }

    /**
     * Closes the WebSocket connection.
     */
    public static void main(String[] args) {
        int port = 8080; // Ensure this matches the server's port
        String uri = "ws://localhost:" + port;

        DataStorage dataStorage = new DataStorage();
        RealTimeDataReader reader = new RealTimeDataReader();

        try {
            reader.readRealTimeData(uri, dataStorage);
        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
