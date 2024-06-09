package com.cardio_generator.outputs;

import com.data_management.DataStorage;
import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;
import static com.data_management.CheckData.isValidData;

import java.net.InetSocketAddress;

/**
 * An OutputStrategy that sends data to a WebSocket server.
 */
public class WebSocketOutputStrategy implements OutputStrategy {

    private WebSocketServer server;

    public WebSocketOutputStrategy(MyWebSocketServer server) {
        this.server = server;
    }

    /**
     * Sends data to the WebSocket server.
     *
     * @param patientId the ID of the patient
     * @param timestamp the timestamp of the data
     * @param label     the label of the data
     * @param data      the data to send
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (isValidData(patientId, timestamp, label, data)) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            server.broadcast(message); // Use the provided server's broadcast method
        } else {
            System.err.println("Invalid data: " + data);
        }
    }

    /**
     * Stores data in the DataStorage object.
     *
     * @param patientId    the ID of the patient
     * @param timestamp    the timestamp of the data
     * @param label        the label of the data
     * @param data         the data to store
     * @param dataStorage  the DataStorage object to store the data
     */
    private void storeData(int patientId, long timestamp, String label, String data, DataStorage dataStorage) {
        // Store data in DataStorage
        double measurement = parseData(data);
        dataStorage.addPatientData(patientId, timestamp, label, measurement);
    }

    /**
     * Parses the data string into a double value.
     *
     * @param data the data string to parse
     * @return the parsed double value
     */
    private double parseData(String data) {
        double measurement;
        if (data.equals("triggered")) {
            measurement = 1.0; // Set some default value for "triggered"
        } else if (data.equals("resolved")) {
            measurement = 0.0; // Set some default value for "resolved"
        } else {
            try {
                measurement = Double.parseDouble(data);
            } catch (NumberFormatException e) {
                // Handle parsing errors, set default value
                measurement = 0.0;
                System.err.println("Error parsing data: " + data);
            }
        }
        return measurement;
    }


    /**
     * A simple WebSocket server that listens for incoming connections.
     */
    private static class SimpleWebSocketServer extends WebSocketServer {

        public SimpleWebSocketServer(InetSocketAddress address) {
            super(address);
        }

        /**
         * Handles a new connection.
         *
         * @param conn      the WebSocket connection
         * @param handshake the handshake data
         */
        @Override
        public void onOpen(WebSocket conn, org.java_websocket.handshake.ClientHandshake handshake) {
            System.out.println("New connection: " + conn.getRemoteSocketAddress());
        }

        /**
         * Handles a closed connection.
         *
         * @param conn   the WebSocket connection
         * @param code   the code indicating the reason for disconnection
         * @param reason the reason for disconnection
         * @param remote indicates if the disconnection was initiated remotely
         */
        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
        }

        /**
         * Handles incoming messages.
         *
         * @param conn    the WebSocket connection
         * @param message the incoming message
         */
        @Override
        public void onMessage(WebSocket conn, String message) {
            // Not used in this context
        }

        /**
         * Handles errors that occur.
         *
         * @param conn the WebSocket connection
         * @param ex   the exception that occurred
         */
        @Override
        public void onError(WebSocket conn, Exception ex) {
            ex.printStackTrace();
        }

        /**
         * Called when the server starts successfully.
         */
        @Override
        public void onStart() {
            System.out.println("Server started successfully");
        }
    }
}
