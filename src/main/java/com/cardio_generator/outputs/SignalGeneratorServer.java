package com.cardio_generator.outputs;

import com.cardio_generator.outputs.WebSocketOutputStrategy;

import java.net.UnknownHostException;

public class SignalGeneratorServer {

    private WebSocketOutputStrategy outputStrategy;

    public SignalGeneratorServer(int port) throws UnknownHostException {
        outputStrategy = new WebSocketOutputStrategy(port);
    }

    public void sendMessage(int patientId, long timestamp, String label, String data) {
        outputStrategy.output(patientId, timestamp, label, data);
    }

    public static void main(String[] args) {
        int port = 8887; // choose your port
        try {
            SignalGeneratorServer server = new SignalGeneratorServer(port);
            server.sendMessage(1, System.currentTimeMillis(), "label", "data");
            System.out.println("SignalGeneratorServer started on port: " + port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}