package alerts;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AlertGeneratorTest {
    private DataStorage mockDataStorage = Mockito.mock(DataStorage.class);
    private AlertGenerator alertGenerator = new AlertGenerator(mockDataStorage);

    @Test
    void evaluateData_NoRecords_NoAlertTriggered() {
        int patientId = 1;
        when(mockDataStorage.getRecords(eq(patientId), anyLong(), anyLong())).thenReturn(Collections.emptyList());

        alertGenerator.evaluateData(patientId);

        verify(alertGenerator, never()).triggerAlert(any());
    }

    @Test
    void evaluateData_BloodPressureStable_NoAlertTriggered() {
        int patientId = 1;
        long currentTime = System.currentTimeMillis();
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(patientId, "BloodPressure", String.valueOf(currentTime - 3500000L), 120),
                new PatientRecord(patientId, "BloodPressure", String.valueOf(currentTime - 3400000L), 120),
                new PatientRecord(patientId, "BloodPressure", String.valueOf(currentTime - 3300000L), 120)
        );

        when(mockDataStorage.getRecords(eq(patientId), anyLong(), anyLong())).thenReturn(records);

        alertGenerator.evaluateData(patientId);

        verify(alertGenerator, never()).triggerAlert(any());
    }

    @Test
    void triggerAlert_WritesToFile_ErrorWritingToFile() throws IOException {
        Alert alert = new Alert(1, "Test Alert", System.currentTimeMillis());

        PrintWriter mockWriter = Mockito.mock(PrintWriter.class);
        when(mockWriter.append(any(CharSequence.class))).thenThrow(new IOException("Test exception"));

        alertGenerator = new AlertGenerator(mockDataStorage) {
            protected PrintWriter getWriter(String filename) throws IOException {
                return mockWriter;
            }
        };

        alertGenerator.triggerAlert(alert);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(System.err, times(1)).println(captor.capture());
        assertTrue(captor.getValue().contains("Error writing alert to log file: Test exception"));
    }
}