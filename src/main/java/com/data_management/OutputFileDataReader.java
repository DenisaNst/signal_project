package com.data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is made to read data from an output file generated
 */
public class OutputFileDataReader implements DataReader {
    private final String outputDirectory;

    public OutputFileDataReader(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * This method checks first if the file exists and actually reads it line by line
     * while storing it in data storage.
     * Also, it extracts relevant information such as patientId, measurement values, etc.
     * @param dataStorage the storage where data will be stored
     * @throws IOException
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        Path outputDirPath = Paths.get(outputDirectory);
        if (!Files.exists(outputDirPath) || !Files.isDirectory(outputDirPath)) {
            throw new IOException("Output directory not found: " + outputDirPath);
        }

        List<Path> outputFiles = Files.list(outputDirPath)
                .filter(path -> path.toString().endsWith(".txt"))
                .collect(Collectors.toList());

        for (Path outputFile : outputFiles) {
            System.out.println("Reading file: " + outputFile.toString());

            try (BufferedReader reader = new BufferedReader(new FileReader(outputFile.toFile()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",");

                    int patientId = Integer.parseInt(fields[0].split(":")[1].trim());
                    long timestamp = Long.parseLong(fields[1].split(":")[1].trim());
                    String recordType = fields[2].split(":")[1].trim();
                    String data = fields[3].split(":")[1].trim();
                    double measurementValue = data.equals("triggered") ? 1.0 : 0.0;

                    dataStorage.addPatientData(patientId, timestamp, recordType, measurementValue);
                }
            }
        }
    }
}
