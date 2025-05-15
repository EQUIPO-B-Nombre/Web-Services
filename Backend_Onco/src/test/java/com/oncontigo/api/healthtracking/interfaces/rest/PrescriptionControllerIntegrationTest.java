package com.oncontigo.api.healthtracking.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oncontigo.api.BackendOncoApplication;
import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.domain.model.entities.Prescription;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.CreatePrescriptionResource;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.UpdatePrescriptionResource;
import com.oncontigo.api.iam.domain.model.aggregates.User;
import com.oncontigo.api.iam.domain.model.commands.SignInCommand;
import com.oncontigo.api.iam.domain.model.commands.SignUpCommand;
import com.oncontigo.api.iam.domain.model.entities.Role;
import com.oncontigo.api.iam.domain.model.valueobjects.Roles;
import com.oncontigo.api.profile.domain.model.commands.CreateDoctorCommand;
import com.oncontigo.api.profile.domain.model.commands.CreatePatientCommand;
import com.oncontigo.api.profile.domain.model.entities.Doctor;
import com.oncontigo.api.profile.domain.model.entities.Patient;
import com.oncontigo.api.profile.infrastructure.persistence.jpa.DoctorRepository;
import com.oncontigo.api.profile.infrastructure.persistence.jpa.PatientRepository;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.PrescriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BackendOncoApplication.class)
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class PrescriptionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private com.oncontigo.api.iam.domain.services.UserCommandService userCommandService;

    private String token;
    private Doctor doctor;
    private Patient patient;

    @BeforeEach
    void setup() {
        Optional<User> doctorUser = userCommandService.handle(
                new SignUpCommand("doctoruser@example.com", "password", List.of(
                        new Role(Roles.ROLE_USER)
                ))
        );

        String token = userCommandService.handle(new SignInCommand("doctoruser@example.com", "password")).orElseThrow().getRight();

        this.token = token;

        CreateDoctorCommand command = new CreateDoctorCommand(doctorUser.get().getId());
        doctor = new Doctor(command, doctorUser.get());
        doctorRepository.save(doctor);

        Optional<User> patientUser = userCommandService.handle(
                new SignUpCommand("patientuser@example.com", "password", List.of(
                        new Role(Roles.ROLE_USER)
                ))
        );

        patient = patientRepository.save(new Patient(new CreatePatientCommand(patientUser.get().getId()), patientUser.get()));
    }

    @Test
    void createPrescription_withValidData_returnsCreated() throws Exception {
        CreatePrescriptionResource requestBody = new CreatePrescriptionResource(
                patient.getId(),
                doctor.getId(),
                "Medication Name",
                "Dosage Information"
        );

        mockMvc.perform(post("/api/v1/prescriptions")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.medicationName").value("Medication Name"));
    }

    @Test
    void getPrescriptionById_withValidId_returnsOk() throws Exception {
        Prescription prescription = prescriptionRepository.save(
                new Prescription(
                        new com.oncontigo.api.healthtracking.domain.model.commands.CreatePrescriptionCommand(
                                patient.getId(), // Agregar patientId
                                doctor.getId(),  // Agregar doctorId
                                "Medication Name",
                                "Dosage Information"
                        ),
                        patient,
                        doctor
                )
        );

        mockMvc.perform(get("/api/v1/prescriptions/" + prescription.getId())
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.medicationName").value("Medication Name"));
    }

    @Test
    void getAllPrescriptionsByPatientId_withValidPatientId_returnsOk() throws Exception {
        // Crear y guardar prescripciones asociadas al paciente
        Prescription prescription1 = prescriptionRepository.save(
                new Prescription(
                        new com.oncontigo.api.healthtracking.domain.model.commands.CreatePrescriptionCommand(
                                patient.getId(), // Agregar patientId
                                doctor.getId(),  // Agregar doctorId
                                "Medication 1",
                                "Dosage Information"
                        ),
                        patient,
                        doctor
                )
        );

        Prescription prescription2 = prescriptionRepository.save(
                new Prescription(
                        new com.oncontigo.api.healthtracking.domain.model.commands.CreatePrescriptionCommand(
                                patient.getId(), // Agregar patientId
                                doctor.getId(),  // Agregar doctorId
                                "Medication 2",
                                "Dosage Information"
                        ),
                        patient,
                        doctor
                )
        );

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(get("/api/v1/prescriptions/patient/" + patient.getId())
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].medicationName").value("Medication 1"))
                .andExpect(jsonPath("$[1].medicationName").value("Medication 2"));
    }

    @Test
    void getAllPrescriptionsByPatientId_withInvalidPatientId_returnsNotFound() throws Exception {
        // Realizar la solicitud con un ID de paciente inexistente y verificar la respuesta
        mockMvc.perform(get("/api/v1/prescriptions/patient/9999")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePrescription_withValidId_returnsOk() throws Exception {
        Prescription prescription = prescriptionRepository.save(
                new Prescription(
                        new com.oncontigo.api.healthtracking.domain.model.commands.CreatePrescriptionCommand(
                                patient.getId(), // Agregar patientId
                                doctor.getId(),  // Agregar doctorId
                                "Old Name",
                                "OId Information"
                        ),
                        patient,
                        doctor
                )
        );

        UpdatePrescriptionResource requestBody = new UpdatePrescriptionResource(
                "Updated Medication",
                "Updated Dosage"
        );

        mockMvc.perform(put("/api/v1/prescriptions/" + prescription.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.medicationName").value("Updated Medication"));
    }

    @Test
    void deletePrescription_withValidId_returnsNoContent() throws Exception {
        Prescription prescription = prescriptionRepository.save(
                new Prescription(
                        new com.oncontigo.api.healthtracking.domain.model.commands.CreatePrescriptionCommand(
                                patient.getId(),
                                doctor.getId(),
                                "To be Deleted",
                                "Dosage "
                        ),
                        patient,
                        doctor
                )
        );

        mockMvc.perform(delete("/api/v1/prescriptions/" + prescription.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}