package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private Alert alert;
    private PatientRecord patientRecord;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        int patientId = patientRecord.getPatientId();
        List<PatientRecord> records = dataStorage.getRecords(patientId, System.currentTimeMillis() - 3600000L, System.currentTimeMillis());

        for (int i = 0; i < records.size(); i++) {
            PatientRecord record = records.get(i);
            String patientId1 = alert.getPatientId();

            // Blood Pressure Data Alerts
            if (record.getRecordType().equals("BloodPressure")) {
                if (i >= 2) {
                    // Trend Alert
                    if (isIncreasingTrend(records, i) || isDecreasingTrend(records, i)) {
                        triggerAlert(new Alert(patientId1, "Blood Pressure Trend Alert", record.getTimestamp()));
                    }
                }
                // Critical Threshold Alert
                double systolicPressure = record.getMeasurementValue();
                double diastolicPressure = records.get(i + 1).getMeasurementValue();
                if (systolicPressure > 180 || systolicPressure < 90 || diastolicPressure > 120 || diastolicPressure < 60) {
                    triggerAlert(new Alert(patientId1, "Critical Blood Pressure Alert", record.getTimestamp()));
                }
            }

            // Blood Saturation Data Alerts
            else if (record.getRecordType().equals("BloodSaturation")) {
                double saturation = record.getMeasurementValue();
                if (saturation < 92) {
                    triggerAlert(new Alert(patientId1, "Low Saturation Alert", record.getTimestamp()));
                }
                if (i >= 6 && isRapidDrop(records, i)) {
                    triggerAlert(new Alert(patientId1, "Rapid Drop Alert", record.getTimestamp()));
                }
            }

            // Combined Alert: Hypotensive Hypoxemia Alert
            else if (record.getRecordType().equals("BloodPressure") && records.get(i + 1).getRecordType().equals("BloodSaturation")) {
                double systolicPressure = record.getMeasurementValue();
                double saturation = records.get(i + 1).getMeasurementValue();
                if (systolicPressure < 90 && saturation < 92) {
                    triggerAlert(new Alert(patientId1, "Hypotensive Hypoxemia Alert", record.getTimestamp()));
                }
            }

            // ECG Data Alerts
            else if (record.getRecordType().equals("ECG")) {
                int heartRate = (int) record.getMeasurementValue();
                if (heartRate < 50 || heartRate > 100) {
                    triggerAlert(new Alert(patientId1, "Abnormal Heart Rate Alert", record.getTimestamp()));
                }
            }


            // Triggered Alert
            // Implementation for triggered alert will depend on the integration with HealthDataGenerator
        }
    }

    private boolean isIncreasingTrend(List<PatientRecord> records, int currentIndex) {
        double previousValue = records.get(currentIndex - 2).getMeasurementValue();
        double currentValue = records.get(currentIndex - 1).getMeasurementValue();
        double nextValue = records.get(currentIndex).getMeasurementValue();

        return (currentValue - previousValue > 10) && (nextValue - currentValue > 10);
    }

    private boolean isDecreasingTrend(List<PatientRecord> records, int currentIndex) {
        double previousValue = records.get(currentIndex - 2).getMeasurementValue();
        double currentValue = records.get(currentIndex - 1).getMeasurementValue();
        double nextValue = records.get(currentIndex).getMeasurementValue();

        return (previousValue - currentValue > 10) && (currentValue - nextValue > 10);
    }

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
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        System.out.println("Alert Triggered:");
        System.out.println("Patient ID: " + alert.getPatientId());
        System.out.println("Condition: " + alert.getCondition());
        System.out.println("Timestamp: " + alert.getTimestamp());

        // Log the alert (example: write alert details to a log file)
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
