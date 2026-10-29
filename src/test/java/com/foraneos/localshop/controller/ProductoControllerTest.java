package com.foraneos.localshop.controller;

import com.foraneos.localshop.model.Producto;
import com.foraneos.localshop.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ProductoRepository repository;

    @Test
    void rendersServerSideCatalog() throws Exception {
        Producto coffee = Producto.builder().nombre("Café de altura")
                .descripcion("Tostado local").precio(145.0).build();
        when(repository.findAll()).thenReturn(List.of(coffee));

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("productos"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Café de altura")));
    }
}
