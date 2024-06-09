package com.data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is made to read data from an output file generated
 */
public class OutputFileDataReader implements DataReader2 {
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
    public void readData(DataStorage dataStorage) throws IOException { // Not overriding any method in the interface
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
                    String measurement1;
                    double measurement2;
                    if(data.equals("triggered")) {
                        measurement1 = "triggered";
                        dataStorage.addPatientData2(patientId, timestamp, recordType, measurement1);
                    } else if(data.equals("resolved")){
                        measurement1 = "resolved";
                        dataStorage.addPatientData2(patientId, timestamp, recordType, measurement1);
                    } else if (data.matches("\\d+\\.\\d+%")) { // Corrected regex for percentage data
                        String numberPart = data.replaceAll("[^\\d.]", "");
                        double measurement = Double.parseDouble(numberPart) / 100.0; // divide by 100 to convert percentage to fraction
                        dataStorage.addPatientData(patientId, timestamp, recordType, measurement);
                    } else {
                        measurement2 = Double.parseDouble(data);
                        dataStorage.addPatientData(patientId, timestamp, recordType, measurement2);
                    }
                }
            }
        }
    }

    /**
     * This method is not supported by this class and will throw an exception if called.
     * @param uri
     * @param dataStorage
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    @Override
    public void readRealTimeData(String uri, DataStorage dataStorage) throws URISyntaxException, InterruptedException {
        throw new UnsupportedOperationException("This class does not support reading real-time data.");
    }
}
