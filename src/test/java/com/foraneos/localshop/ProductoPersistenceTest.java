package com.foraneos.localshop;

import com.foraneos.localshop.model.Producto;
import com.foraneos.localshop.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:products;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ProductoPersistenceTest {

    @Autowired
    private ProductoRepository repository;

    @Autowired
    private CommandLineRunner seedProducts;

    @Test
    void persistsSixProductsWithoutDuplicatingSeed() throws Exception {
        List<Producto> products = repository.findAll();
        assertThat(products).hasSize(6);
        assertThat(products).extracting(Producto::getNombre).doesNotHaveDuplicates();

        seedProducts.run();

        assertThat(repository.count()).isEqualTo(6);
    }
}
