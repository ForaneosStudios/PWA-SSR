package com.foraneos.localshop.repository;

import com.foraneos.localshop.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
