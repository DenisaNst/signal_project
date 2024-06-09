package com.alerts.strategy;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;

/**
 * Strategy for the blood pressure alert
 */
public class OxygenSaturationStrategy extends AlertGenerator implements AlertStrategy{

    private DataStorage dataStorage;

    public OxygenSaturationStrategy(DataStorage dataStorage) {
        super(dataStorage);
        this.dataStorage = dataStorage;
    }

    @Override
    public void checkAlert(int patientId) {
        super.evaluateData(patientId);

    }
}