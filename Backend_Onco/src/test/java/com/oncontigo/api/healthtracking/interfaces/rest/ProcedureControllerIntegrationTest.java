package com.oncontigo.api.healthtracking.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oncontigo.api.BackendOncoApplication;
import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateHealthTrackingCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Procedure;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.CreateProcedureResource;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.UpdateProcedureResource;
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
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.HealthTrackingRepository;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.ProcedureRepository;
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
class ProcedureControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProcedureRepository procedureRepository;

    @Autowired
    private HealthTrackingRepository healthTrackingRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserCommandService userCommandService;

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
    void createProcedure_withValidData_returnsCreated() throws Exception {
        CreateProcedureResource requestBody = new CreateProcedureResource(
                "Procedure Name",
                "Procedure Description",
                LocalDateTime.now(),
                healthTracking.getId()
        );

        mockMvc.perform(post("/api/v1/procedures")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Procedure Name"));
    }

    @Test
    void getProcedureById_withValidId_returnsOk() throws Exception {
        Procedure procedure = procedureRepository.save(
                new Procedure(
                        new com.oncontigo.api.healthtracking.domain.model.commands.CreateProcedureCommand(
                                "Procedure Name",
                                "Procedure Description",
                                LocalDateTime.now(),
                                healthTracking.getId()
                        ),
                        healthTracking
                )
        );

        mockMvc.perform(get("/api/v1/procedures/" + procedure.getId())
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(procedure.getId()));
    }

    @Test
    void getAllProceduresByHealthTrackingId_withValidHealthTrackingId_returnsOk() throws Exception {
        Procedure procedure1 = procedureRepository.save(
                new Procedure(
                        new com.oncontigo.api.healthtracking.domain.model.commands.CreateProcedureCommand(
                                "Procedure 1",
                                "Description 1",
                                LocalDateTime.now(),
                                healthTracking.getId()
                        ),
                        healthTracking
                )
        );

        Procedure procedure2 = procedureRepository.save(
                new Procedure(
                        new com.oncontigo.api.healthtracking.domain.model.commands.CreateProcedureCommand(
                                "Procedure 2",
                                "Description 2",
                                LocalDateTime.now(),
                                healthTracking.getId()
                        ),
                        healthTracking
                )
        );

        mockMvc.perform(get("/api/v1/procedures/healthtracking/" + healthTracking.getId())
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Procedure 1"))
                .andExpect(jsonPath("$[1].name").value("Procedure 2"));
    }

    @Test
    void updateProcedure_withValidId_returnsOk() throws Exception {
        Procedure procedure = procedureRepository.save(
                new Procedure(
                        new com.oncontigo.api.healthtracking.domain.model.commands.CreateProcedureCommand(
                                "Old Name",
                                "Old Description",
                                LocalDateTime.now(),
                                healthTracking.getId()
                        ),
                        healthTracking
                )
        );

        UpdateProcedureResource requestBody = new UpdateProcedureResource(
                "Updated Name",
                "Updated Description",
                LocalDateTime.now()
        );

        mockMvc.perform(put("/api/v1/procedures/" + procedure.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void deleteProcedure_withValidId_returnsNoContent() throws Exception {
        Procedure procedure = procedureRepository.save(
                new Procedure(
                        new com.oncontigo.api.healthtracking.domain.model.commands.CreateProcedureCommand(
                                "To be Deleted",
                                "Description",
                                LocalDateTime.now(),
                                healthTracking.getId()
                        ),
                        healthTracking
                )
        );

        mockMvc.perform(delete("/api/v1/procedures/" + procedure.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}