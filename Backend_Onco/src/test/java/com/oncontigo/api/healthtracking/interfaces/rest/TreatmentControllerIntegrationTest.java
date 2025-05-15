package com.oncontigo.api.healthtracking.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oncontigo.api.BackendOncoApplication;
import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateHealthTrackingCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Treatment;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.CreateTreatmentResource;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.UpdateTreatmentResource;
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
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.HealthTrackingRepository;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.TreatmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BackendOncoApplication.class)
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class TreatmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TreatmentRepository treatmentRepository;

    @Autowired
    private HealthTrackingRepository healthTrackingRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private com.oncontigo.api.iam.domain.services.UserCommandService userCommandService;

    private String token;
    private HealthTracking healthTracking;

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
        Doctor doctor = new Doctor(command, doctorUser.get());
        doctorRepository.save(doctor);

        Optional<User> patientUser = userCommandService.handle(
                new SignUpCommand("patientuser@example.com", "password", List.of(
                        new Role(Roles.ROLE_USER)
                ))
        );

        Patient patient = patientRepository.save(new Patient(new CreatePatientCommand(patientUser.get().getId()), patientUser.get()));

        healthTracking = healthTrackingRepository.save(
                new HealthTracking(
                        new CreateHealthTrackingCommand(patient.getId(), doctor.getId(), "Health Tracking Description"),
                        patient,
                        doctor
                )
        );
    }

    @Test
    void createTreatment_withValidData_returnsCreated() throws Exception {
        CreateTreatmentResource requestBody = new CreateTreatmentResource(
                "Treatment Name",
                "Treatment Description",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                healthTracking.getId()
        );

        mockMvc.perform(post("/api/v1/treatments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Treatment Name"));
    }

    @Test
    void createTreatment_withInvalidToken_returnsUnauthorized() throws Exception {
        CreateTreatmentResource requestBody = new CreateTreatmentResource(
                "Treatment Name",
                "Treatment Description",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                healthTracking.getId()
        );

        mockMvc.perform(post("/api/v1/treatments")
                        .header("Authorization", "Bearer invalid_token") // Token inválido
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getTreatmentById_withValidId_returnsOk() throws Exception {
        Treatment treatment = treatmentRepository.save(
                new Treatment(
                        new com.oncontigo.api.healthtracking.domain.model.commands.CreateTreatmentCommand(
                                "Treatment Name",
                                "Treatment Description",
                                LocalDateTime.now(),
                                LocalDateTime.now().plusDays(10),
                                healthTracking.getId() // Agregar el healthTrackingId
                        ),
                        healthTracking
                )
        );

        mockMvc.perform(get("/api/v1/treatments/" + treatment.getId())
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Treatment Name"));
    }

    @Test
    void getTreatmentById_withInvalidId_returnsNotFound() throws Exception {
        // Realizar la solicitud con un ID inexistente y verificar la respuesta
        mockMvc.perform(get("/api/v1/treatments/9999") // ID inexistente
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllTreatmentsByHealthTrackingId_withValidHealthTrackingId_returnsOk() throws Exception {
        // Crear y guardar tratamientos asociados al HealthTracking
        Treatment treatment1 = treatmentRepository.save(
                new Treatment(
                        new com.oncontigo.api.healthtracking.domain.model.commands.CreateTreatmentCommand(
                                "Treatment 1",
                                "Description 1",
                                LocalDateTime.now(),
                                LocalDateTime.now().plusDays(5),
                                healthTracking.getId()
                        ),
                        healthTracking
                )
        );
        Treatment treatment2 = treatmentRepository.save(
                new Treatment(
                        new com.oncontigo.api.healthtracking.domain.model.commands.CreateTreatmentCommand(
                                "Treatment 2",
                                "Description 2",
                                LocalDateTime.now().plusDays(1),
                                LocalDateTime.now().plusDays(6),
                                healthTracking.getId()
                        ),
                        healthTracking
                )
        );

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(get("/api/v1/treatments/healthtracking/" + healthTracking.getId())
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Treatment 1"))
                .andExpect(jsonPath("$[1].name").value("Treatment 2"));
    }

    @Test
    void getAllTreatmentsByHealthTrackingId_withInvalidHealthTrackingId_returnsNotFound() throws Exception {
        // Realizar la solicitud con un ID de HealthTracking inexistente y verificar la respuesta
        mockMvc.perform(get("/api/v1/treatments/healthtracking/9999")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTreatment_withValidId_returnsOk() throws Exception {
        Treatment treatment = treatmentRepository.save(
                new Treatment(
                        new com.oncontigo.api.healthtracking.domain.model.commands.CreateTreatmentCommand(
                                "Old Name",
                                "Old Description",
                                LocalDateTime.now(),
                                LocalDateTime.now().plusDays(10),
                                healthTracking.getId() // Agregar el healthTrackingId
                        ),
                        healthTracking
                )
        );

        UpdateTreatmentResource requestBody = new UpdateTreatmentResource(
                "Updated Name",
                "Updated Description",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(15)
        );

        mockMvc.perform(put("/api/v1/treatments/" + treatment.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void deleteTreatment_withValidId_returnsNoContent() throws Exception {
        Treatment treatment = treatmentRepository.save(
                new Treatment(
                        new com.oncontigo.api.healthtracking.domain.model.commands.CreateTreatmentCommand(
                                "To be Deleted",
                                "To be Deleted",
                                LocalDateTime.now(),
                                LocalDateTime.now().plusDays(10),
                                healthTracking.getId()
                        ),
                        healthTracking
                )
        );

        mockMvc.perform(delete("/api/v1/treatments/" + treatment.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}