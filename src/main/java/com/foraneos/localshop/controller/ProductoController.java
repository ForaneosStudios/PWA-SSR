package com.foraneos.localshop.controller;

import com.foraneos.localshop.repository.ProductoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductoController {

    private final ProductoRepository repository;

    public ProductoController(ProductoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String catalog(Model model) {
        model.addAttribute("productos", repository.findAll());
        return "index";
    }
}
