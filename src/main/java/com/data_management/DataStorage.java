package com.data_management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alerts.AlertGenerator;

/**
 * Manages storage and retrieval of patient data within a healthcare monitoring system.
 * This class serves as a repository for all patient records, organized by patient IDs.
 */
public class DataStorage {
    private Map<Integer, Patient> patientMap; // Stores patient objects indexed by their unique patient ID.

    /**
     * Constructs a new instance of DataStorage, initializing the underlying storage structure.
     */
    public DataStorage() {
        this.patientMap = new HashMap<>();
    }

    /**
     * Adds or updates patient data in the storage.
     * If the patient does not exist, a new Patient object is created and added to the storage.
     * Otherwise, the new data is added to the existing patient's records.
     *
     * @param patientId        the unique identifier of the patient
     * @param measurementValue the value of the health metric being recorded
     * @param recordType       the type of record, e.g., "HeartRate", "BloodPressure"
     * @param timestamp        the time at which the measurement was taken, in milliseconds since the Unix epoch
     */
    public void addPatientData(int patientId, long timestamp, String recordType, double measurementValue) {
        Patient patient = patientMap.get(patientId);
        if (patient == null) {
            patient = new Patient(patientId);
            patientMap.put(patientId, patient);
        }
        patient.addRecord(timestamp, recordType, measurementValue);
    }
    public void addPatientData2(int patientId, long timestamp, String recordType, String measurementValue) {
        Patient patient = patientMap.get(patientId);
        if (patient == null) {
            patient = new Patient(patientId);
            patientMap.put(patientId, patient);
        }
        patient.addRecord2(timestamp, recordType, measurementValue);
    }

    /**
     * Retrieves a list of PatientRecord objects for a specific patient, filtered by a time range.
     *
     * @param patientId the unique identifier of the patient whose records are to be retrieved
     * @param startTime the start of the time range, in milliseconds since the Unix epoch
     * @param endTime   the end of the time range, in milliseconds since the Unix epoch
     * @return a list of PatientRecord objects that fall within the specified time range
     */
    public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
        Patient patient = patientMap.get(patientId);
        if (patient != null) {
            return patient.getRecords(startTime, endTime);
        }
        return new ArrayList<>(); // return an empty list if no patient is found
    }

    /**
     * Retrieves a collection of all patients stored in the data storage.
     *
     * @return a list of all patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientMap.values());
    }

    /**
     * The main method for the DataStorage class.
     * Initializes the system, reads data into storage, and continuously monitors and evaluates patient data.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        DataStorage storage = new DataStorage();

        // Initialize the OutputFileDataReader with the output directory
        // Use an absolute path here to ensure correctness
        String outputDirectory = "C:/Users/Admin/Desktop/University/Period 5/Software engineering/signal_project/output";
        OutputFileDataReader reader = new OutputFileDataReader(outputDirectory);

        try {
            reader.readData(storage);

            List<PatientRecord> records = storage.getRecords(1, 1700000000000L, 1800000000000L);
            for (PatientRecord record : records) {
                System.out.print("Patient ID: " + record.getPatientId() +
                        ", Timestamp: " + record.getTimestamp() +
                        ", Label: " + record.getRecordType() +
                        ", Data: ");
                if (record.getMeasurementValue2() != null) {
                    System.out.println(record.getMeasurementValue2());
                } else {
                    System.out.println(record.getMeasurementValue());
                }
            }

            AlertGenerator alertGenerator = new AlertGenerator(storage);

            for (Patient patient : storage.getAllPatients()) {
                alertGenerator.evaluateData(patient.getPatientId());
            }

        } catch (IOException e) {
            System.err.println("Error reading data from output file: " + e.getMessage());
        }
    }
}
