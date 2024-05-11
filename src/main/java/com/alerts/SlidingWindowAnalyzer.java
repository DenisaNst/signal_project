package com.alerts;

import com.data_management.PatientRecord;

import java.util.List;

public class SlidingWindowAnalyzer {
    private int windowSize;

    public SlidingWindowAnalyzer(int windowSize) {
        this.windowSize = windowSize;
    }

    public void analyzePatientData(List<PatientRecord> patientData, int stepSize) {
        for (int i = 0; i <= patientData.size() - windowSize; i += stepSize) {
            List<PatientRecord> windowData = patientData.subList(i, i + windowSize);
            double movingAverage = calculateMovingAverage(windowData);
        }
    }

    private double calculateMovingAverage(List<PatientRecord> windowData) {
        double sum = 0.0;
        for (PatientRecord record : windowData) {
            sum += record.getMeasurementValue();
        }
        return sum / windowData.size();
    }
}
