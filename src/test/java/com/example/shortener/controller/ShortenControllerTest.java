package com.example.shortener.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShortenControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @Nested
     class GetAllUrls {
        @Test
        void should_get_all_urls_success() throws Exception {


            mockMvc.perform(get("/api/urls")
                            .header("Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NjI4ODAzMjEsImVtYWlsIjoidGFuZ0Bob3RtYWlsLmNvbSJ9.mvm8gMoZul9PZOW0jnPpC4xF5hDvUdr3GJl1Tjn6nHNSxxpAQ64Fqj6YavlB-QAL4Mdas3JzET5h-GeNCPnq1_9BWBng4i3CUaKphzdMhH1grtgJhr6PTSX7Jr5D_uK53dsP-xBXBotPDeifSv2gmGI3F8De9ihzvHS1t0E4lIh0lxQvPaymdjw1aV0ZCcVUwo0hr_gzi5mserbh87ahkkmSR5zvJaxmfs-aZs7sOhpCaxRtF-y2cqlPulSnf-Q26wkXi1qCw63iV7bJj22evHYQorZHNUEoC-24J6jDwiCeXwawuvEq7MXYYTBfl4Kwyezzwn7GyP45qQRfZ54-Aw")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.response_code").value("200-000"));
        }
    }

}