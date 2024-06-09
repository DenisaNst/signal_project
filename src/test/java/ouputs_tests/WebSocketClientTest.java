package ouputs_tests;

import com.cardio_generator.outputs.MyWebSocketClient;
import com.data_management.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.net.URISyntaxException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

/**
 * Tests for the WebSocket client.
 */
public class WebSocketClientTest {
    private MyWebSocketClient client;
    private DataStorage dataStorage;

    /**
     * Sets up the test environment.
     *
     * @throws URISyntaxException if the URI is invalid
     */
    @BeforeEach
    public void setup() throws URISyntaxException {
        dataStorage = Mockito.mock(DataStorage.class);
        client = new MyWebSocketClient(new URI("ws://localhost:8080"), dataStorage);
    }

    /**
     * Tests that the client sends a message to the server when the connection is opened.
     */
    @Test
    public void onMessage_storesValidMessage() {
        client.onMessage("1,1000,HeartRate,80.0");
        verify(dataStorage, times(1)).addPatientData(anyInt(), anyLong(), anyString(), anyDouble());
    }

    /**
     * Tests that the client does not store an invalid message.
     */
    @Test
    public void onMessage_doesNotStoreInvalidMessage() {
        client.onMessage("invalid message");
        verify(dataStorage, never()).addPatientData(anyInt(), anyLong(), anyString(), anyDouble());
    }

    /**
     * Tests that the client handles an exception when storing a message.
     */
    @Test
    public void onMessage_handlesException() {
        doThrow(new RuntimeException("Error processing message")).when(dataStorage).addPatientData(anyInt(), anyLong(), anyString(), anyDouble());
        client.onMessage("1,1000,HeartRate,80.0");
        verify(dataStorage, times(1)).addPatientData(anyInt(), anyLong(), anyString(), anyDouble());
    }
}