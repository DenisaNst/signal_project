package com.cardio_generator.outputs;

import com.data_management.DataReader2;
import com.data_management.DataStorage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import static com.data_management.CheckData.isValidMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * WebSocket client that connects to a server and processes messages received from the server.
 */
public class MyWebSocketClient extends WebSocketClient implements DataReader2 {

    private DataStorage dataStorage;

    public MyWebSocketClient(URI serverUri, DataStorage dataStorage) {
        super(serverUri);
        this.dataStorage = dataStorage;
    }

    /**
     * Sends a message to the server when the connection is opened.
     *
     * @param handshakedata Information about the handshake process
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("Connected");
    }

    /**
     * Processes messages received from the server.
     *
     * @param message The message received from the server
     */
    @Override
    public void onMessage(String message) {
        try {
            String[] parts = message.split(",");

            if (parts.length == 4) {

                int patientId = Integer.parseInt(parts[0]);
                long timestamp = Long.parseLong(parts[1]);
                String recordType = parts[2];

                if(parts[3].contains("%")) {
                    parts[3] = parts[3].replace("%", " ");
                }

                if(parts[3].equals("triggered")){
                    parts[3]="1";
                }

                if(parts[3].equals("resolved")){
                    parts[3]="0";
                }

                double measurementValue = Double.parseDouble(parts[3]);

                dataStorage.addPatientData(patientId, timestamp, recordType, measurementValue);
            } else {
                System.err.println("Invalid message format: " + message);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing message: " + message);
            e.printStackTrace();
        }
    }

    /**
     * Handles the case when the connection to the server is closed.
     *
     * @param code   The code indicating the reason for the connection closure
     * @param reason The reason for the connection closure
     * @param remote Whether the connection was closed remotely
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed: " + reason);
    }

    /**
     * Handles errors that occur during the WebSocket communication.
     *
     * @param ex The exception that occurred
     */
    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void readRealTimeData(String uri, DataStorage dataStorage) throws URISyntaxException, InterruptedException {
        this.dataStorage = dataStorage;
        try {
            this.connect();
        } catch (Exception e) {
            System.err.println("Failed to connect to WebSocket server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
