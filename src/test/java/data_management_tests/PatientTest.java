package data_management_tests;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PatientTest {

    /**
     * Tests the addRecord and getRecords methods of the Patient class.
     */
    @Test
    void testAddAndGetRecords() {
        Patient patient = new Patient(1);

        patient.addRecord(120, "HeartRate", 1621908000000L); // Heart rate record
        patient.addRecord(140, "HeartRate", 1621908100000L); // Heart rate record
        patient.addRecord(130, "HeartRate", 1621908200000L); // Heart rate record
        patient.addRecord(120, "BloodPressure", 1621908300000L); // Blood pressure record
        patient.addRecord(110, "BloodPressure", 1621908400000L); // Blood pressure record
        patient.addRecord(100, "BloodPressure", 1621908500000L); // Blood pressure record

        List<PatientRecord> records = patient.getRecords(1621908100000L, 1621908400000L);

        assertEquals(4, records.size());

        assertEquals(140, records.get(0).getMeasurementValue());
        assertEquals(130, records.get(1).getMeasurementValue());
        assertEquals(120, records.get(2).getMeasurementValue());
        assertEquals(110, records.get(3).getMeasurementValue());
    }
}
