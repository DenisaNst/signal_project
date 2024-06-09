package com.main;

import com.cardio_generator.HealthDataSimulator;
import com.data_management.DataStorage;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length > 0 && args[0].equals("DataStorage")) {
            System.out.println("Trying to generate data with the websocket server...");
            DataStorage.main(new String[]{});
        } else {
            System.out.println("Generating data from the health simulator...");
            HealthDataSimulator.main(new String[]{});
        }
    }
}