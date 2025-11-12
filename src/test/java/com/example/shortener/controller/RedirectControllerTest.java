package com.example.shortener.controller;

import com.example.shortener.repository.ShortenUrlRepository;
import com.example.shortener.repository.entity.ShortenUrlEntity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class RedirectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShortenUrlRepository shortenUrlRepository;
    @Nested
    class RedirectTests {
        @Test
        void should_redirect_to_original_url_correctly() throws Exception {
            Mockito.when(shortenUrlRepository.findByShortUrl("http://localhost:8080/shortener/r/abc123")).thenReturn(
                    new ShortenUrlEntity(){{
                        setId(1L);
                        setOriginalUrl("https://www.google.com");
                        setShortUrl("http://localhost:8080/shortener/r/abc123");
                    }}
            );
            mockMvc.perform(get("/r/{shortenUrl}","abc123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("https://www.google.com"));
        }

        @Test
        void should_cannot_redirect_to_original_url_when_short_url_incorrect() throws Exception {
            Mockito.when(shortenUrlRepository.findByShortUrl("http://localhost:8080/shortener/r/abc123")).thenReturn(null);
            mockMvc.perform(get("/r/{shortenUrl}","abc123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.response_code").value("404-001"))
                .andExpect(jsonPath("$.response_message").value("page not found"))
                .andExpect(jsonPath("$.data").isEmpty());
        }
    }
}