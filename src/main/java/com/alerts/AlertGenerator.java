package com.alerts;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * This class generates alerts based on the patient's health data.
 */
public class AlertGenerator {
    private DataStorage dataStorage;

    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the patient's health data and triggers alerts if necessary.
     *
     * @param patientId the ID of the patient
     */
    public void evaluateData(int patientId) {
        List<PatientRecord> records = dataStorage.getRecords(patientId, System.currentTimeMillis() - 3600000L, System.currentTimeMillis());

        for (int i = 0; i < records.size(); i++) {
            PatientRecord record = records.get(i);
            String recordType = record.getRecordType();

            if (recordType.equals("BloodPressure")) {
                evaluateBloodPressure(records, i, patientId);
            } else if (recordType.equals("BloodSaturation")) {
                evaluateBloodSaturation(records, i, patientId);
            } else if (recordType.equals("ECG")) {
                evaluateECG(records, i, patientId);
            }
        }
    }

    /**
     * Evaluates the patient's blood pressure data and triggers alerts if necessary.
     *
     * @param records    the patient's health records
     * @param currentIndex the index of the current record
     * @param patientId  the ID of the patient
     */
    private void evaluateBloodPressure(List<PatientRecord> records, int currentIndex, int patientId) {
        PatientRecord record = records.get(currentIndex);
        if (currentIndex >= 2) {
            if (isIncreasingTrend(records, currentIndex) || isDecreasingTrend(records, currentIndex)) {
                triggerAlert(new Alert(patientId, "Blood Pressure Trend Alert", record.getTimestamp()));
            }
        }
        double systolicPressure = record.getMeasurementValue();
        double diastolicPressure = records.get(currentIndex).getMeasurementValue();
        if (systolicPressure > 180 || systolicPressure < 90 || diastolicPressure > 120 || diastolicPressure < 60) {
            triggerAlert(new Alert(patientId, "Critical Blood Pressure Alert", record.getTimestamp()));
        }
    }

    /**
     * Evaluates the patient's blood saturation data and triggers alerts if necessary.
     *
     * @param records    the patient's health records
     * @param currentIndex the index of the current record
     * @param patientId  the ID of the patient
     */
    private void evaluateBloodSaturation(List<PatientRecord> records, int currentIndex, int patientId) {
        PatientRecord record = records.get(currentIndex);
        double saturation = record.getMeasurementValue();
        if (saturation < 92) {
            triggerAlert(new Alert(patientId, "Low Saturation Alert", record.getTimestamp()));
        }
        if (currentIndex >= 6 && isRapidDrop(records, currentIndex)) {
            triggerAlert(new Alert(patientId, "Rapid Drop Alert", record.getTimestamp()));
        }
    }

    /**
     * Evaluates the patient's ECG data and triggers alerts if necessary.
     *
     * @param records    the patient's health records
     * @param currentIndex the index of the current record
     * @param patientId  the ID of the patient
     */
    private void evaluateECG(List<PatientRecord> records, int currentIndex, int patientId) {
        PatientRecord record = records.get(currentIndex);
        int heartRate = (int) record.getMeasurementValue();
        if (heartRate < 50 || heartRate > 100) {
            triggerAlert(new Alert(patientId, "Abnormal Heart Rate Alert", record.getTimestamp()));
        }
    }

    /**
     * Checks if the blood pressure is in an increasing trend.
     *
     * @param records    the patient's health records
     * @param currentIndex the index of the current record
     * @return true if the blood pressure is in an increasing trend, false otherwise
     */
    private boolean isIncreasingTrend(List<PatientRecord> records, int currentIndex) {
        double previousValue = records.get(currentIndex - 2).getMeasurementValue();
        double currentValue = records.get(currentIndex - 1).getMeasurementValue();
        double nextValue = records.get(currentIndex).getMeasurementValue();

        return (currentValue - previousValue > 10) && (nextValue - currentValue > 10);
    }

    /**
     * Checks if the blood pressure is in a decreasing trend.
     *
     * @param records    the patient's health records
     * @param currentIndex the index of the current record
     * @return true if the blood pressure is in a decreasing trend, false otherwise
     */
    private boolean isDecreasingTrend(List<PatientRecord> records, int currentIndex) {
        double previousValue = records.get(currentIndex - 2).getMeasurementValue();
        double currentValue = records.get(currentIndex - 1).getMeasurementValue();
        double nextValue = records.get(currentIndex).getMeasurementValue();

        return (previousValue - currentValue > 10) && (currentValue - nextValue > 10);
    }

    /**
     * Checks if the blood saturation is rapidly dropping.
     *
     * @param records    the patient's health records
     * @param currentIndex the index of the current record
     * @return true if the blood saturation is rapidly dropping, false otherwise
     */
    private boolean isRapidDrop(List<PatientRecord> records, int currentIndex) {
        double currentSaturation = records.get(currentIndex).getMeasurementValue();
        double previousSaturation = records.get(currentIndex - 1).getMeasurementValue();
        long currentTime = records.get(currentIndex).getTimestamp();
        long previousTime = records.get(currentIndex - 1).getTimestamp();

        long timeDifferenceMinutes = (currentTime - previousTime) / (1000 * 60);

        double saturationDrop = (previousSaturation - currentSaturation) / previousSaturation * 100;

        return saturationDrop >= 5 && timeDifferenceMinutes <= 10;
    }

    /**
     * Triggers an alert for the patient.
     *
     * @param alert the alert to trigger
     */
    public void triggerAlert(Alert alert) {
        System.out.println("Alert Triggered:");
        System.out.println("Patient ID: " + alert.getPatientId());
        System.out.println("Condition: " + alert.getCondition());
        System.out.println("Timestamp: " + alert.getTimestamp());

        try (PrintWriter writer = new PrintWriter(new FileWriter("alert_log.txt", true))) {
            writer.println("Alert Triggered:");
            writer.println("Patient ID: " + alert.getPatientId());
            writer.println("Condition: " + alert.getCondition());
            writer.println("Timestamp: " + alert.getTimestamp());
            writer.println();
        } catch (IOException e) {
            System.err.println("Error writing alert to log file: " + e.getMessage());
        }
    }
}