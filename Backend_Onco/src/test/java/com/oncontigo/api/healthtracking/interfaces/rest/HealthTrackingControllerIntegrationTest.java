package com.oncontigo.api.healthtracking.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oncontigo.api.BackendOncoApplication;
import com.oncontigo.api.healthtracking.application.outboundservices.acl.ExternalProfilesService;
import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateHealthTrackingCommand;
import com.oncontigo.api.healthtracking.domain.services.HealthTrackingCommandService;
import com.oncontigo.api.healthtracking.domain.services.HealthTrackingQueryService;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.HealthTrackingRepository;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.CreateHealthTrackingResource;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.UpdateHealthTrackingResource;
import com.oncontigo.api.iam.domain.model.aggregates.User;
import com.oncontigo.api.iam.domain.model.commands.SignInCommand;
import com.oncontigo.api.iam.domain.model.commands.SignUpCommand;
import com.oncontigo.api.iam.domain.model.entities.Role;
import com.oncontigo.api.iam.domain.model.valueobjects.Roles;
import com.oncontigo.api.iam.domain.services.UserCommandService;
import com.oncontigo.api.profile.domain.model.commands.CreateDoctorCommand;
import com.oncontigo.api.profile.domain.model.commands.CreatePatientCommand;
import com.oncontigo.api.profile.domain.model.entities.Doctor;
import com.oncontigo.api.profile.domain.model.entities.Patient;
import com.oncontigo.api.profile.infrastructure.persistence.jpa.DoctorRepository;
import com.oncontigo.api.profile.infrastructure.persistence.jpa.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BackendOncoApplication.class)
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class HealthTrackingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HealthTrackingCommandService healthTrackingCommandService;

    @Autowired
    private HealthTrackingQueryService healthTrackingQueryService;

    @Autowired
    private UserCommandService userCommandService;

    @Autowired
    private ExternalProfilesService externalProfilesService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private HealthTrackingRepository healthTrackingRepository;

    private String token;
    private Doctor doctor;
    private Patient patient;
    @Autowired
    private ObjectMapper objectMapper;


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

        this.doctor = doctor;
        this.patient = patient;


    }

    @Test
    void getHealthTrackingById_withValidId_returnsOk() throws Exception {
        // Crear y guardar el HealthTracking con datos completos
        HealthTracking healthTracking = healthTrackingRepository.save(
                new HealthTracking(
                        new CreateHealthTrackingCommand(patient.getId(), doctor.getId(), "Test Description"),
                        patient,
                        doctor
                )
        );
        long healthTrackingId = healthTracking.getId();

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(get("/api/v1/healthtrackings/" + healthTrackingId)
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    void getAllHealthTrackingsByDoctorId_withValidDoctorId_returnsOk() throws Exception {
        // Crear y guardar HealthTrackings asociados al doctor
        HealthTracking healthTracking1 = healthTrackingRepository.save(
                new HealthTracking(
                        new CreateHealthTrackingCommand(patient.getId(), doctor.getId(), "Description 1"),
                        patient,
                        doctor
                )
        );
        HealthTracking healthTracking2 = healthTrackingRepository.save(
                new HealthTracking(
                        new CreateHealthTrackingCommand(patient.getId(), doctor.getId(), "Description 2"),
                        patient,
                        doctor
                )
        );

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(get("/api/v1/healthtrackings/doctor/" + doctor.getId())
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[1].description").value("Description 2"));
    }

    @Test
    void getAllHealthTrackingsByDoctorId_withInvalidToken_returnsUnauthorized() throws Exception {
        // Realizar la solicitud con un token inválido y verificar la respuesta
        mockMvc.perform(get("/api/v1/healthtrackings/doctor/" + doctor.getId())
                        .header("Authorization", "Bearer invalid_token")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createHealthTracking_withValidData_returnsCreated() throws Exception {

        CreateHealthTrackingResource requestBody = new CreateHealthTrackingResource(
                patient.getId(),
                doctor.getId(),
                "New Health Tracking"
        );

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(post("/api/v1/healthtrackings")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("New Health Tracking"));
    }

    @Test
    void createHealthTracking_withInvalidToken_returnsUnauthorized() throws Exception {

        CreateHealthTrackingResource requestBody = new CreateHealthTrackingResource(
                patient.getId(),
                doctor.getId(),
                "New Health Tracking"
        );

        // Realizar la solicitud con un token inválido y verificar la respuesta
        mockMvc.perform(post("/api/v1/healthtrackings")
                        .header("Authorization", "Bearer invalid_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateHealthTracking_withValidId_returnsOk() throws Exception {
        // Crear y guardar un HealthTracking
        HealthTracking healthTracking = healthTrackingRepository.save(
                new HealthTracking(
                        new CreateHealthTrackingCommand(patient.getId(), doctor.getId(), "Old Description"),
                        patient,
                        doctor
                )
        );

        UpdateHealthTrackingResource requestBody = new UpdateHealthTrackingResource(
                "INACTIVE",
                "Updated Description",
                LocalDateTime.now()
        );

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(put("/api/v1/healthtrackings/" + healthTracking.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))) // Convertir a JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    void updateHealthTracking_withInvalidToken_returnsUnauthorized() throws Exception {
        // Crear y guardar un HealthTracking
        HealthTracking healthTracking = healthTrackingRepository.save(
                new HealthTracking(
                        new CreateHealthTrackingCommand(patient.getId(), doctor.getId(), "Old Description"),
                        patient,
                        doctor
                )
        );

        UpdateHealthTrackingResource requestBody = new UpdateHealthTrackingResource(
                "INACTIVE",
                "Updated Description",
                LocalDateTime.now()
        );

        // Realizar la solicitud con un token inválido y verificar la respuesta
        mockMvc.perform(put("/api/v1/healthtrackings/" + healthTracking.getId())
                        .header("Authorization", "Bearer invalid_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteHealthTracking_withValidId_returnsNoContent() throws Exception {
        // Crear y guardar un HealthTracking
        HealthTracking healthTracking = healthTrackingRepository.save(
                new HealthTracking(
                        new CreateHealthTrackingCommand(patient.getId(), doctor.getId(), "To be deleted"),
                        patient,
                        doctor
                )
        );

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(delete("/api/v1/healthtrackings/" + healthTracking.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteHealthTracking_withInvalidToken_returnsUnauthorized() throws Exception {
        // Crear y guardar un HealthTracking
        HealthTracking healthTracking = healthTrackingRepository.save(
                new HealthTracking(
                        new CreateHealthTrackingCommand(patient.getId(), doctor.getId(), "To be deleted"),
                        patient,
                        doctor
                )
        );

        // Realizar la solicitud con un token inválido y verificar la respuesta
        mockMvc.perform(delete("/api/v1/healthtrackings/" + healthTracking.getId())
                        .header("Authorization", "Bearer invalid_token"))
                .andExpect(status().isUnauthorized());
    }


}