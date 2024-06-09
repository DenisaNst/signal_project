package data_management_tests;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatientTest {
    private Patient patient;

    @BeforeEach
    public void setUp() {
        patient = new Patient(1);
    }

    @Test
    public void testAddRecord() {
        patient.addRecord(1623254400000L, "HeartRate", 75.0);
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        assertEquals(1, records.size());
        PatientRecord record = records.get(0);
        assertEquals(1, record.getPatientId());
        assertEquals(75.0, record.getMeasurementValue());
        assertEquals("HeartRate", record.getRecordType());
        assertEquals(1623254400000L, record.getTimestamp());
    }

    @Test
    public void testGetRecordsWithinRange() {
        patient.addRecord(1623254400000L, "HeartRate", 75.0);
        patient.addRecord(1623254500000L, "BloodPressure", 120.0);
        patient.addRecord(1623254600000L, "HeartRate", 80.0);
        List<PatientRecord> records = patient.getRecords(1623254500000L, 1623254600000L);
        assertEquals(2, records.size());
        assertEquals("BloodPressure", records.get(0).getRecordType());
        assertEquals("HeartRate", records.get(1).getRecordType());
    }
}
