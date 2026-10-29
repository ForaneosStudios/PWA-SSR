package com.foraneos.localshop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:pwa")
class PwaAssetsTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void servesValidManifestAndPngIcons() throws Exception {
        String manifest = mvc.perform(get("/manifest.json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andReturn().getResponse().getContentAsString();
        assertThat(objectMapper.readTree(manifest).get("short_name").asText()).isEqualTo("LocalShop");

        mvc.perform(get("/images/icon-192.png"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/png"));
        mvc.perform(get("/images/icon-512.png"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/png"));
    }

    @Test
    void servesOfflineResources() throws Exception {
        mvc.perform(get("/sw.js"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("fetch(request)")));
        mvc.perform(get("/offline.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Volveremos a encontrarnos")));
    }

    @Test
    void servesGeolocationScript() throws Exception {
        mvc.perform(get("/js/geolocation.js"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("getCurrentPosition")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("PERMISSION_DENIED")));
    }
}
