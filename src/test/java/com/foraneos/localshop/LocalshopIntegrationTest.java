package com.foraneos.localshop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:integration;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class LocalshopIntegrationTest {

    @Autowired MockMvc mvc;

    @Test
    void rendersSeededCatalogAndPwaResources() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Café de altura")))
                .andExpect(content().string(containsString("manifest.json")))
                .andExpect(content().string(containsString("geolocation.js")));
        mvc.perform(get("/manifest.json")).andExpect(status().isOk());
        mvc.perform(get("/sw.js")).andExpect(status().isOk());
        mvc.perform(get("/css/styles.css")).andExpect(status().isOk());
    }
}
