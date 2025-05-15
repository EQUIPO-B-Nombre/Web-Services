package com.oncontigo.api.healthtracking.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oncontigo.api.BackendOncoApplication;
import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateAppointmentCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateHealthTrackingCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Appointment;
import com.oncontigo.api.healthtracking.domain.services.AppointmentCommandService;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.AppointmentRepository;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.HealthTrackingRepository;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.CreateAppointmentResource;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.UpdateAppointmentResource;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BackendOncoApplication.class)
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class AppointmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppointmentRepository appointmentRepository;

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
    void getAppointmentById_withValidId_returnsOk() throws Exception {
        // Crear y guardar una cita
        Appointment appointment = appointmentRepository.save(
                new Appointment(
                        new CreateAppointmentCommand(healthTracking.getId(), "Test Appointment", LocalDateTime.now()),
                        healthTracking
                )
        );

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(get("/api/v1/appointments/" + appointment.getId())
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Test Appointment"));
    }

    @Test
    void getAppointmentById_withInvalidId_returnsNotFound() throws Exception {
        // Realizar la solicitud con un ID inexistente y verificar la respuesta
        mockMvc.perform(get("/api/v1/appointments/9999")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllAppointmentsByHealthTrackingId_withValidHealthTrackingId_returnsOk() throws Exception {
        // Crear y guardar citas asociadas al HealthTracking
        Appointment appointment1 = appointmentRepository.save(
                new Appointment(
                        new CreateAppointmentCommand(healthTracking.getId(), "Appointment 1", LocalDateTime.now()),
                        healthTracking
                )
        );
        Appointment appointment2 = appointmentRepository.save(
                new Appointment(
                        new CreateAppointmentCommand(healthTracking.getId(), "Appointment 2", LocalDateTime.now().plusDays(1)),
                        healthTracking
                )
        );

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(get("/api/v1/appointments/healthtracking/" + healthTracking.getId())
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Appointment 1"))
                .andExpect(jsonPath("$[1].description").value("Appointment 2"));
    }

    @Test
    void getAllAppointmentsByHealthTrackingId_withInvalidHealthTrackingId_returnsNotFound() throws Exception {
        // Realizar la solicitud con un ID de HealthTracking inexistente y verificar la respuesta
        mockMvc.perform(get("/api/v1/appointments/healthtracking/9999")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createAppointment_withValidData_returnsCreated() throws Exception {
        CreateAppointmentResource requestBody = new CreateAppointmentResource(
                "Appointment Description",
                healthTracking.getId(),
                LocalDateTime.now()
        );

        mockMvc.perform(post("/api/v1/appointments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Appointment Description"));
    }

    @Test
    void createAppointment_withInvalidToken_returnsUnauthorized() throws Exception {
        CreateAppointmentResource requestBody = new CreateAppointmentResource(
                "Appointment Description",
                healthTracking.getId(),
                LocalDateTime.now()
        );

        mockMvc.perform(post("/api/v1/appointments")
                        .header("Authorization", "Bearer invalid_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteAppointment_withValidId_returnsNoContent() throws Exception {
        Appointment appointment = appointmentRepository.save(
                new Appointment(
                        new CreateAppointmentCommand(healthTracking.getId(), "To be deleted", LocalDateTime.now()),
                        healthTracking
                )
        );

        mockMvc.perform(delete("/api/v1/appointments/" + appointment.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAppointment_withInvalidToken_returnsUnauthorized() throws Exception {
        Appointment appointment = appointmentRepository.save(
                new Appointment(
                        new CreateAppointmentCommand(healthTracking.getId(), "To be deleted", LocalDateTime.now()),
                        healthTracking
                )
        );

        mockMvc.perform(delete("/api/v1/appointments/" + appointment.getId())
                        .header("Authorization", "Bearer invalid_token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateAppointment_withValidId_returnsOk() throws Exception {
        Appointment appointment = appointmentRepository.save(
                new Appointment(
                        new CreateAppointmentCommand(healthTracking.getId(), "Old Update", LocalDateTime.now()),
                        healthTracking
                )
        );

        UpdateAppointmentResource requestBody = new UpdateAppointmentResource(
                "Updated Description",
                LocalDateTime.now().plusDays(1),
                "COMPLETED"
        );

        mockMvc.perform(put("/api/v1/appointments/" + appointment.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void updateAppointment_withInvalidToken_returnsUnauthorized() throws Exception {
        Appointment appointment = appointmentRepository.save(
                new Appointment(
                        new CreateAppointmentCommand(healthTracking.getId(), "Old Update", LocalDateTime.now()),
                        healthTracking
                )
        );

        UpdateAppointmentResource requestBody = new UpdateAppointmentResource(
                "Updated Description",
                LocalDateTime.now().plusDays(1),
                "COMPLETED"
        );

        mockMvc.perform(put("/api/v1/appointments/" + appointment.getId())
                        .header("Authorization", "Bearer invalid_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isUnauthorized());
    }

}
