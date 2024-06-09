package com.cardio_generator.outputs;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;

/**
 * The MyWebSocketServer class extends the WebSocketServer class
 * and provides functionality to create a WebSocket server.
 */
public class MyWebSocketServer extends WebSocketServer {

    public MyWebSocketServer(InetSocketAddress address) {
        super(address);
    }

    /**
     * Sends a welcome message to the client when a new connection is established.
     *
     * @param conn is the WebSocket connection.
     * @param handshake is the handshake data.
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("Welcome to the server!"); //This method sends a message to the new client
    }

    /**
     * Handles when a client disconnects.
     *
     * @param conn is the WebSocket connection.
     * @param code is the code indicating the reason for disconnection.
     * @param reason is the reason for disconnection.
     * @param remote indicates if the disconnection was initiated remotely.
     */
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
    }

    /**
     * Handles incoming messages from clients.
     *
     * @param conn is the WebSocket connection.
     * @param message is the incoming message.
     */
    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received message from " + conn.getRemoteSocketAddress() + ": " + message);
    }

    /**
     * Handles any errors that occur.
     *
     * @param conn is the WebSocket connection.
     * @param ex is the exception that occurred.
     */
    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("An error occurred on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
    }

    /**
     * Prints a message when the server is started.
     */
    @Override
    public void onStart() {
        System.out.println("Server started!");
    }
}