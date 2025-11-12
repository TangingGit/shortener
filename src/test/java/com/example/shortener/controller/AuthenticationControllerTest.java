package com.example.shortener.controller;

import com.example.shortener.repository.UserRepository;
import com.example.shortener.repository.entity.UserEntity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;
    @Nested
    class Register {
        @Test
        void should_register_success() throws Exception {
            String requestBody = """
                {
                    "email": "test@hotmail.com",
                    "password": "1234"
                }
            """;
            Mockito.when(userRepository.findById("tang@hotmail.com")).thenReturn(Optional.empty());
            Mockito.when(userRepository.save(any())).thenReturn(new UserEntity());
            mockMvc.perform(
                post("/api/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.response_code").value("200-000"))
                    .andExpect(jsonPath("$.response_message").value("success"))
                    .andExpect(jsonPath("$.data").doesNotExist())
            ;

        }

        @Test
        void should_register_unsuccess_when_user_is_exist() throws Exception {
            String requestBody = """
                {
                    "email": "tang@hotmail.com",
                    "password": "1234"
                }
            """;
            Mockito.when(userRepository.findById("tang@hotmail.com")).thenReturn(Optional.of(new UserEntity(){{
                setEmail("test@hotmail.com");
                setPassword("hashed_password");
            }}));
            Mockito.when(userRepository.save(any())).thenReturn(new UserEntity());
            mockMvc.perform(
                            post("/api/register")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.response_code").value("400-001"))
                    .andExpect(jsonPath("$.response_message").value("user is exist"))
                    .andExpect(jsonPath("$.data").doesNotExist())
            ;

        }

        @Test
        void should_invalid_request_when_email_invalid_format() throws Exception {
            String requestBody = """
                {
                    "email": "testhotmail.com",
                    "password": "1234"
                }
            """;
            Mockito.when(userRepository.findById("tang@hotmail.com")).thenReturn(Optional.of(new UserEntity(){{
                setEmail("tang@hotmail.com");
                setPassword("hashed_password");
            }}));
            Mockito.when(userRepository.save(any())).thenReturn(new UserEntity());
            mockMvc.perform(
                            post("/api/register")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.response_code").value("400-002"))
                    .andExpect(jsonPath("$.response_message").value("invalid format"))
                    .andExpect(jsonPath("$.data").doesNotExist())
            ;

        }
    }

    @Nested
    class Login {
        @Test
        void should_login_success() throws Exception {
            String requestBody = """
                {
                    "email": "test@hotmail.com",
                    "password": "1234"
                }
            """;
            Mockito.when(userRepository.findByEmailAndPassword(any(), any())).thenReturn(new UserEntity(){{
                setPassword("hashed_password");
                setEmail("test@hotmail.com");
            }});
            mockMvc.perform(
                            post("/api/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.response_code").value("200-000"))
                    .andExpect(jsonPath("$.response_message").value("success"))
                    .andExpect(jsonPath("$.data.token").exists());
        }

        @Test
        void should_cannot_login_when_email_or_password_incorrect() throws Exception {
            String requestBody = """
                {
                    "email": "test@hotmail.com",
                    "password": "1234"
                }
            """;
            Mockito.when(userRepository.findByEmailAndPassword(any(), any())).thenReturn(null);
            mockMvc.perform(
                            post("/api/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody)
                    )
                    .andExpect(status().isForbidden())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.response_code").value("403-001"))
                    .andExpect(jsonPath("$.response_message").value("user or password invalid"))
                    .andExpect(jsonPath("$.data").doesNotExist());
        }

        @Test
        void should_invalid_request_when_email_invalid_format() throws Exception {
            String requestBody = """
                {
                    "email": "testhotmail.com",
                    "password": "1234"
                }
            """;
            Mockito.when(userRepository.findByEmailAndPassword(any(), any())).thenReturn(null);
            mockMvc.perform(
                            post("/api/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.response_code").value("400-002"))
                    .andExpect(jsonPath("$.response_message").value("invalid format"))
                    .andExpect(jsonPath("$.data").doesNotExist());
        }
    }
}