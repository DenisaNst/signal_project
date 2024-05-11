package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.util.List;

class DataStorageTest {

    @Test
    void testAddAndGetRecords() {
        // DataReader reader
            DataStorage storage = new DataStorage();

            // Add some sample patient data
            storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
            storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

            // Retrieve records for patient ID 1 within the specified time range
            List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);

            // Assert statements to validate the results
            assertEquals(2, records.size()); // Check if two records are retrieved
            assertEquals(100.0, records.get(0).getMeasurementValue()); // Validate the measurement value of the first record

    }
}
