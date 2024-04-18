package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * The PatientDataGenerator interface defines a contract for classes that generate
 * health data for a patient.
 */
public interface PatientDataGenerator {
    /**
     *Generates random data for patients and displays the output strategy.
     *
     * @param patientId is the patient's ID.
     * @param outputStrategy is the output strategy to use for displaying the generated data.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
