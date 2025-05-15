package com.oncontigo.api.iam.interfaces.rest;

import com.oncontigo.api.BackendOncoApplication;
import com.oncontigo.api.iam.domain.model.entities.Role;
import com.oncontigo.api.iam.interfaces.rest.resources.SignInResource;
import com.oncontigo.api.iam.interfaces.rest.resources.SignUpResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BackendOncoApplication.class)
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void signUp_withValidData_returnsCreated() throws Exception {
        // Arrange
        var signUpResource = new SignUpResource("newuser@example.com", "password", List.of("ROLE_USER"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/authentication/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpResource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newuser@example.com"));
    }

    @Test
    void signIn_withValidCredentials_returnsOk() throws Exception {
        // Arrange
        var signUpResource = new SignUpResource("testuser@example.com", "password", List.of("ROLE_USER"));
        mockMvc.perform(post("/api/v1/authentication/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpResource)));

        var signInResource = new SignInResource("testuser@example.com", "password");

        // Act & Assert
        mockMvc.perform(post("/api/v1/authentication/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInResource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void signInWithInvalidCredentialsReturnsBadRequest() throws Exception {
        // Arrange
        SignInResource signIn = new SignInResource("nonexistent@test.com", "wrongpassword");
        // Act
        mockMvc.perform(post("/api/v1/authentication/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signIn)))
                // Assert
                .andExpect(status().isBadRequest());
    }
}