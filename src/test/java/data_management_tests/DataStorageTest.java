package data_management_tests;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataStorageTest {

    private DataStorage dataStorage;

    @BeforeEach
    void setUp() {
        dataStorage = new DataStorage();
    }


    @Test
    void addPatientData_NewPatient() {
        dataStorage.addPatientData(1, 1622136000000L, "BloodPressure", 120);
        List<PatientRecord> records = dataStorage.getRecords(1, 0, Long.MAX_VALUE);

        assertEquals(1, records.size());
        assertEquals(1622136000000L, records.get(0).getTimestamp());
        assertEquals("BloodPressure", records.get(0).getRecordType());
        assertEquals(120, records.get(0).getMeasurementValue());
    }

    @Test
    void addPatientData_ExistingPatient() {
        dataStorage.addPatientData(1, 1622136000000L, "BloodPressure", 120);
        dataStorage.addPatientData(1, 1622137000000L, "BloodPressure", 130);
        List<PatientRecord> records = dataStorage.getRecords(1, 0, Long.MAX_VALUE);

        assertEquals(2, records.size());
        assertEquals(1622136000000L, records.get(0).getTimestamp());
        assertEquals("BloodPressure", records.get(0).getRecordType());
        assertEquals(120, records.get(0).getMeasurementValue());
        assertEquals(1622137000000L, records.get(1).getTimestamp());
        assertEquals("BloodPressure", records.get(1).getRecordType());
        assertEquals(130, records.get(1).getMeasurementValue());
    }

    @Test
    void getRecords_Empty() {
        List<PatientRecord> records = dataStorage.getRecords(1, 0, Long.MAX_VALUE);

        assertEquals(0, records.size());
    }


    @Test
    void addPatientData_ConcurrentUpdates() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                dataStorage.addPatientData(1, 1622136000000L + i, "HeartRate", 80.0 + i);
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                dataStorage.addPatientData(1, 1622136000000L + i, "BloodPressure", 120.0 + i);
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        assertEquals(2000, dataStorage.getRecords(1, 0, Long.MAX_VALUE).size());
    }

    @Test
    void addPatientData2_AddsNewPatientData() {
        dataStorage.addPatientData2(1, 1000L, "HeartRate", "80.0");
        assertFalse(dataStorage.getAllPatients().isEmpty());
    }

    @Test
    void addPatientData2_ExistingPatient() {
        dataStorage.addPatientData2(1, 1000L, "HeartRate", "80.0");
        dataStorage.addPatientData2(1, 2000L, "HeartRate", "85.0");
        assertEquals(2, dataStorage.getRecords(1, 0, Long.MAX_VALUE).size());
    }
}
