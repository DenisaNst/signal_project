package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;
/**
 * The FileOutputStrategy class implements the OutputStrategy interface
 * and provides functionality to output health data to files.
 */
public class FileOutputStrategy implements OutputStrategy {
    /**
     * Represents the base directory where files will be stored.
     */
    //Changed method name to baseDirectory
    private String baseDirectory;

    /**
     * Maps labels to file paths.
     * This map is used to associate each label describing the type of data with its corresponding file path.
     */
    //Changed variable name to fileMap
    // Use the Map interface instead of ConcurrentHashMap
    public final Map<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Constructs a new FileOutputStrategy with the specified base directory.
     *
     * @param baseDirectory is the base directory where output files will be stored.
     */
    public FileOutputStrategy(String baseDirectory) {

        this.baseDirectory = baseDirectory;
    }

    /**
     * Outputs health data for a patient to a file.
     *
     * @param patientId is the patient's ID.
     * @param timestamp is the time stamp at which the health data was generated.
     * @param label is the label describing the type of data from HealthDataSimulator.
     * @param data is the actual health data.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the filePath variable
        //Changed variable name to filePath
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}