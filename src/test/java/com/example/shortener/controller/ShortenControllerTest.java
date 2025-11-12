package com.example.shortener.controller;

import com.example.shortener.repository.ShortenUrlRepository;
import com.example.shortener.repository.entity.ShortenUrlEntity;
import com.example.shortener.service.AuthenticationService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShortenControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationService authenticationService;

    @MockitoBean
    private ShortenUrlRepository shortenUrlRepository;


    @Nested
     class GetAllUrls {
        List<ShortenUrlEntity> shortenUrlEntityList = List.of(
            new ShortenUrlEntity(){{
                setId(1L);
                setOriginalUrl("https://www.google.com");
                setShortUrl("http://localhost:8080/shortener/r/b");
            }}
        );
        @Test
        void should_get_all_urls_success() throws Exception {

            String token = authenticationService.getToken("test@gmail.com");
            Mockito.when(shortenUrlRepository.findAll()).thenReturn(shortenUrlEntityList);
            mockMvc.perform(get("/api/urls")
                            .header("Authorization","Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.response_code").value("200-000"))
                    .andExpect(jsonPath("$.response_message").value("success"))
                    .andExpect(jsonPath("$.data[0].id").value(1L))
                    .andExpect(jsonPath("$.data[0].original_url").value("https://www.google.com"))
                    .andExpect(jsonPath("$.data[0].short_url").value("http://localhost:8080/shortener/r/b"));
        }

        @Test
        void should_cannot_get_all_urls_when_token_expire() throws Exception {

            mockMvc.perform(get("/api/urls")
                            .header("Authorization","Bearer " + "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NjI4ODAzMjEsImVtYWlsIjoidGFuZ0Bob3RtYWlsLmNvbSJ9.mvm8gMoZul9PZOW0jnPpC4xF5hDvUdr3GJl1Tjn6nHNSxxpAQ64Fqj6YavlB-QAL4Mdas3JzET5h-GeNCPnq1_9BWBng4i3CUaKphzdMhH1grtgJhr6PTSX7Jr5D_uK53dsP-xBXBotPDeifSv2gmGI3F8De9ihzvHS1t0E4lIh0lxQvPaymdjw1aV0ZCcVUwo0hr_gzi5mserbh87ahkkmSR5zvJaxmfs-aZs7sOhpCaxRtF-y2cqlPulSnf-Q26wkXi1qCw63iV7bJj22evHYQorZHNUEoC-24J6jDwiCeXwawuvEq7MXYYTBfl4Kwyezzwn7GyP45qQRfZ54-Aw")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.response_code").value("403-002"))
                    .andExpect(jsonPath("$.response_message").value("jwt invalid"))
                    .andExpect(jsonPath("$.data").doesNotExist());
        }

        @Test
        void should_cannot_get_all_urls_when_token_invalid() throws Exception {

            mockMvc.perform(get("/api/urls")
                            .header("Authorization","Bearer " + "invalid.token")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.response_code").value("403-002"))
                    .andExpect(jsonPath("$.response_message").value("jwt invalid"))
                    .andExpect(jsonPath("$.data").doesNotExist());
        }
    }

    @Nested
    class Shorten {
        List<ShortenUrlEntity> shortenUrlEntityList = List.of(
                new ShortenUrlEntity(){{
                    setId(1L);
                    setOriginalUrl("https://www.google.com");
                    setShortUrl("http://localhost:8080/shortener/r/b");
                }}
        );
        @Test
        void should_collect_shorten_url_success() throws Exception {
            String requestBody = """
                {
                    "original_url":"https://www.google.com"
                }
            """;
            Mockito.when(shortenUrlRepository.findByOriginalUrl("https://www.google.com")).thenReturn(null);
            Mockito.when(shortenUrlRepository.getNextSequenceValue()).thenReturn(1L);
            Mockito.when(shortenUrlRepository.save(any())).thenReturn(new ShortenUrlEntity());
            mockMvc.perform(post("/api/shorten")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.response_code").value("200-000"))
                    .andExpect(jsonPath("$.response_message").value("success"))
                    .andExpect(jsonPath("$.data.original_url").value("https://www.google.com"))
                    .andExpect(jsonPath("$.data.short_url").value("http://localhost:8080/shortener/r/b"));
        }

        @Test
        void should_return_success_when_original_url_exist() throws Exception {
            String requestBody = """
                {
                    "original_url":"https://www.google.com"
                }
            """;
            Mockito.when(shortenUrlRepository.findByOriginalUrl("https://www.google.com")).thenReturn(new ShortenUrlEntity(){{
                setId(1L);
                setOriginalUrl("https://www.google.com");
                setShortUrl("http://localhost:8080/shortener/r/exist");
            }});
            mockMvc.perform(post("/api/shorten")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.response_code").value("200-000"))
                    .andExpect(jsonPath("$.response_message").value("success"))
                    .andExpect(jsonPath("$.data.original_url").value("https://www.google.com"))
                    .andExpect(jsonPath("$.data.short_url").value("http://localhost:8080/shortener/r/exist"));
        }

        @Test
        void should_throw_invalid_format_when_send_original_url_invalid_format() throws Exception {
            String requestBody = """
                {
                    "original_url":"https://www.googlecom"
                }
            """;
            mockMvc.perform(post("/api/shorten")
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

    @Nested
    class DeleteUrl {
        @Test
        void should_delete_url_shorten_success() throws Exception {
            Mockito.doNothing().when(shortenUrlRepository).deleteById(1L);
            String token = authenticationService.getToken("test@gmail.com");
            mockMvc.perform(delete("/api/urls/{id}",1L)
                            .header("Authorization","Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.response_code").value("200-000"))
                    .andExpect(jsonPath("$.response_message").value("success"))
                    .andExpect(jsonPath("$.data").doesNotExist());
        }
    }
}