package alerts_test;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AlertGeneratorTest {

    @Mock
    private DataStorage dataStorage;

    @Captor
    private ArgumentCaptor<Alert> alertCaptor;

    private AlertGenerator alertGenerator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        alertGenerator = new AlertGenerator(dataStorage);
    }

    @Test
    public void testNoRecords_NoAlertTriggered() {
        int patientId = 1;
        when(dataStorage.getRecords(eq(patientId), anyLong(), anyLong())).thenReturn(Collections.emptyList());

        alertGenerator.evaluateData(patientId);

        verify(dataStorage).getRecords(eq(patientId), anyLong(), anyLong());
        verifyNoMoreInteractions(dataStorage);
    }

    @Test
    public void testNormalBloodPressure_NoAlertTriggered() {
        int patientId = 1;
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(patientId, 23.00, "BloodPressure", 120L),
                new PatientRecord(patientId, 28.00, "BloodPressure", 157L)
        );

        when(dataStorage.getRecords(eq(patientId), anyLong(), anyLong())).thenReturn(records);

        alertGenerator.evaluateData(patientId);

        verify(dataStorage).getRecords(eq(patientId), anyLong(), anyLong());
        verifyNoMoreInteractions(dataStorage);
    }

}