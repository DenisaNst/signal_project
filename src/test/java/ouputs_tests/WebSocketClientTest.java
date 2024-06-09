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
     * Tests that the client handles a message with an invalid number format.
     */
    @Test
    public void onMessage_handlesNumberFormatException() {
        client.onMessage("1,1000,HeartRate,invalid_number");
        verify(dataStorage, never()).addPatientData(anyInt(), anyLong(), anyString(), anyDouble());
    }

    /**
     * Tests that the client handles a message with an invalid number of parts.
     */
    @Test
    public void onMessage_handlesInvalidPartsCount() {
        client.onMessage("1,1000,HeartRate");
        verify(dataStorage, never()).addPatientData(anyInt(), anyLong(), anyString(), anyDouble());
    }

    /**
     * Tests that the client handles a message with a percentage sign in the measurement value.
     */
    @Test
    public void onMessage_handlesPercentageSign() {
        client.onMessage("1,1000,HeartRate,80%");
        verify(dataStorage, times(1)).addPatientData(anyInt(), anyLong(), anyString(), eq(80.0));
    }

    /**
     * Tests that the client handles a message with a "triggered" status.
     */
    @Test
    public void onMessage_handlesTriggeredStatus() {
        client.onMessage("1,1000,HeartRate,triggered");
        verify(dataStorage, times(1)).addPatientData(anyInt(), anyLong(), anyString(), eq(1.0));
    }

    /**
     * Tests that the client handles a message with a "resolved" status.
     */
    @Test
    public void onMessage_handlesResolvedStatus() {
        client.onMessage("1,1000,HeartRate,resolved");
        verify(dataStorage, times(1)).addPatientData(anyInt(), anyLong(), anyString(), eq(0.0));
    }
}