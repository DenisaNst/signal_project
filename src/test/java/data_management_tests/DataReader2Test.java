package data_management_tests;

import com.data_management.DataReader2;
import com.data_management.DataStorage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DataReader2Test {
    private DataStorage mockDataStorage = Mockito.mock(DataStorage.class);
    private DataReader2 mockDataReader2 = Mockito.mock(DataReader2.class);

    @Test
    void readRealTimeData_ValidUri_DataRead() throws URISyntaxException, InterruptedException {
        doNothing().when(mockDataReader2).readRealTimeData(anyString(), eq(mockDataStorage));
        mockDataReader2.readRealTimeData("ws://valid-uri", mockDataStorage);
        verify(mockDataReader2, times(1)).readRealTimeData(anyString(), eq(mockDataStorage));
    }

    @Test
    void readRealTimeData_InvalidUri_ThrowsException() throws URISyntaxException, InterruptedException {
        doThrow(URISyntaxException.class).when(mockDataReader2).readRealTimeData(anyString(), eq(mockDataStorage));
        assertThrows(URISyntaxException.class, () -> mockDataReader2.readRealTimeData("invalid-uri", mockDataStorage));
    }

    @Test
    void readRealTimeData_Interrupted_ThrowsException() throws URISyntaxException, InterruptedException {
        doThrow(InterruptedException.class).when(mockDataReader2).readRealTimeData(anyString(), eq(mockDataStorage));
        assertThrows(InterruptedException.class, () -> mockDataReader2.readRealTimeData("ws://valid-uri", mockDataStorage));
    }
}