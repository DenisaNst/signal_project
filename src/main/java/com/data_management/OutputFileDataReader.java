package com.data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class is made to read data from an output file generated
 */
public class OutputFileDataReader implements DataReader {
    private final String outputDirectory;

    public OutputFileDataReader(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * This method check first if the file exists and actually read it line by line
     * while storing it in data storage.
     * Also, it extract relevant information such as patientId, measurement values, etc.
     * @param dataStorage the storage where data will be stored
     * @throws IOException
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        Path outputFile = Paths.get(outputDirectory, "output.txt");

        if (!Files.exists(outputFile)) {
            throw new IOException("Output file not found: " + outputFile);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                // Extract relevant information from the fields
                int patientId = Integer.parseInt(fields[0].trim());
                double measurementValue = Double.parseDouble(fields[1].trim());
                String recordType = fields[2].trim();
                long timestamp = Long.parseLong(fields[3].trim());

                dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
            }
        }
    }
}

