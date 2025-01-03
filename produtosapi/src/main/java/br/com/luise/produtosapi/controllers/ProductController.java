package br.com.luise.produtosapi.controllers;

import br.com.luise.produtosapi.models.Product;
import br.com.luise.produtosapi.repositories.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable String id) {
        return productRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Product save(@RequestBody Product product) {
        UUID uuid = UUID.randomUUID();
        product.setId(uuid.toString());
        productRepository.save(product);

        return product;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        productRepository.deleteById(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> update(@PathVariable String id, @RequestBody Product product) {
        product.setId(id);
        productRepository.save(product);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public List<Product> findByName(@RequestParam("name") String name) {
        return productRepository.findByName(name);
    }
}
