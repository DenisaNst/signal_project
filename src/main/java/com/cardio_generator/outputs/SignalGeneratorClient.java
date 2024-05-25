package com.cardio_generator.outputs;

import com.data_management.DataStorage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class SignalGeneratorClient extends WebSocketClient {

    private DataStorage dataStorage;

    public SignalGeneratorClient(URI serverUri, DataStorage dataStorage) {
        super(serverUri);
        this.dataStorage = dataStorage;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to server");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received data: " + message);
        parseAndStoreMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("An error occurred:" + ex);
    }

    private void parseAndStoreMessage(String message) {
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

    public static void main(String[] args) {
        int port = 12345;  // Replace with the actual port number your server is running on
        String uri = "ws://localhost:" + port;

        DataStorage dataStorage = new DataStorage();  // Ensure you have an instance of DataStorage

        try {
            SignalGeneratorClient client = new SignalGeneratorClient(new URI(uri), dataStorage);
            client.connectBlocking();
        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}