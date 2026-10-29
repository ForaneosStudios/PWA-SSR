package com.foraneos.localshop.config;

import com.foraneos.localshop.model.Producto;
import com.foraneos.localshop.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedProducts(ProductoRepository repository) {
        return args -> {
            if (repository.count() != 0) {
                return;
            }
            repository.saveAll(List.of(
                    product("Café de altura", "Café artesanal tostado localmente.", 145.00, null),
                    product("Miel regional", "Miel pura de productores de la región.", 120.00, null),
                    product("Pan artesanal", "Hogaza horneada diariamente con masa madre.", 75.00, null),
                    product("Mermelada casera", "Fruta de temporada sin conservadores.", 95.00, null),
                    product("Chocolate de mesa", "Chocolate tradicional con cacao mexicano.", 110.00, null),
                    product("Canasta tejida", "Artesanía elaborada a mano por la comunidad.", 280.00, null)
            ));
        };
    }

    private Producto product(String nombre, String descripcion, Double precio, String imagenUrl) {
        return Producto.builder()
                .nombre(nombre)
                .descripcion(descripcion)
                .precio(precio)
                .imagenUrl(imagenUrl)
                .build();
    }
}
