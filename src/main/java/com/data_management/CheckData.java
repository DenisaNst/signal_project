package com.data_management;

/**
 * CheckData class contains methods to validate data and messages.
 */
public class CheckData {
    /**
     * Check if the data is valid based on the following criteria: patientId and timestamp must be non-negative,
     * label and data must be non-empty.
     * @param patientId is the unique identifier of the patient
     * @param timestamp is the time at which the measurement was taken, in milliseconds since the Unix epoch
     * @param label is the type of record, e.g., "HeartRate", "BloodPressure"
     * @param data is the value of the health metric being recorded
     * @return true if the data is valid, false otherwise
     */
    public static boolean isValidData(int patientId, long timestamp, String label, String data) {
        if (patientId < 0 || timestamp < 0) {
            return false;
        }
        if (label == null || label.isEmpty()) {
            return false;
        }
        if (data == null || data.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Check if the message is valid based on the following criteria: it must contain 4 parts separated by commas,
     * the first part must be an integer, the second part must be a long, and the fourth part must be a double.
     * @param message is the message to be validated
     *                in the format: "patientId,timestamp,label,data"
     * @return true if the message is valid, false otherwise
     */
    public static boolean isValidMessage(String message) {
        String[] parts = message.split(",");
        if (parts.length != 4) {
            return false;
        }
        try {
            int patientId = Integer.parseInt(parts[0]);
            long timestamp = Long.parseLong(parts[1]);
            double measurement = Double.parseDouble(parts[3]);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
