package alerts;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlertGeneratorTest {
    private DataStorage dataStorage;
    private AlertGenerator alertGenerator;

    @BeforeEach
    void setUp() {
        dataStorage = new MockDataStorage();
        alertGenerator = new AlertGenerator(dataStorage);
    }

    @Test
    void testEvaluateData() {
        Patient patient = new Patient(1);
        patient.addRecord(120, "HeartRate", System.currentTimeMillis());
        patient.addRecord(140, "BloodPressure", System.currentTimeMillis());
        patient.addRecord(91, "BloodSaturation", System.currentTimeMillis());

        alertGenerator.evaluateData(patient);

        List<Alert> alerts = ((MockDataStorage) dataStorage).getGeneratedAlerts();

        assertEquals(1, alerts.size());

        Alert alert = alerts.get(0);
        assertEquals("1", alert.getPatientId());
        assertEquals("Low Saturation Alert", alert.getCondition());
    }

    private static class MockDataStorage extends DataStorage implements alerts.MockDataStorage {
        private List<Alert> generatedAlerts = new ArrayList<>();

        @Override
        public void addPatientData(int patientId, double measurementValue, String recordType, long timestamp) {

        }

        @Override
        public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
            return new ArrayList<>();
        }

        @Override
        public void triggerAlert(Alert alert) {
            generatedAlerts.add(alert);
        }

        public List<Alert> getGeneratedAlerts() {
            return generatedAlerts;
        }
    }
}
