package com.apteka24.repositories;

import com.apteka24.models.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    private Product newProduct(String name, String category, boolean active) {
        Product p = new Product();
        p.setName(name);
        p.setDescription("desc");
        p.setPrice(new BigDecimal("1.00"));
        p.setStock(1);
        p.setCategory(category);
        p.setActive(active);
        return p;
    }

    @Test
    void findByActiveTrue_filters() {
        productRepository.saveAll(List.of(
                newProduct("A","C1", true),
                newProduct("B","C1", false)
        ));
        List<Product> active = productRepository.findByActiveTrue();
        assertThat(active).extracting(Product::getName).containsExactly("A");
    }

    @Test
    void findByCategoryAndActiveTrue_filters() {
        productRepository.saveAll(List.of(
                newProduct("A","C1", true),
                newProduct("B","C2", true),
                newProduct("C","C1", false)
        ));
        List<Product> products = productRepository.findByCategoryAndActiveTrue("C1");
        assertThat(products).extracting(Product::getName).containsExactly("A");
    }

    @Test
    void findByNameContainingIgnoreCaseAndActiveTrue_searches() {
        productRepository.saveAll(List.of(
                newProduct("Aspirin","C1", true),
                newProduct("ASP-Other","C1", true),
                newProduct("Hidden","C1", false)
        ));
        List<Product> products = productRepository.findByNameContainingIgnoreCaseAndActiveTrue("asp");
        assertThat(products).extracting(Product::getName).containsExactlyInAnyOrder("Aspirin", "ASP-Other");
    }

    @Test
    void findDistinctCategories_onlyActive() {
        productRepository.saveAll(List.of(
                newProduct("A","C1", true),
                newProduct("B","C2", true),
                newProduct("C","C1", false)
        ));
        List<String> categories = productRepository.findDistinctCategories();
        assertThat(categories).containsExactlyInAnyOrder("C1","C2");
    }

    @Test
    void findByIdAndActiveTrue_respectsActive() {
        Product active = productRepository.save(newProduct("A","C1", true));
        Product inactive = productRepository.save(newProduct("B","C1", false));

        Optional<Product> foundActive = productRepository.findByIdAndActiveTrue(active.getId());
        Optional<Product> foundInactive = productRepository.findByIdAndActiveTrue(inactive.getId());
        assertThat(foundActive).isPresent();
        assertThat(foundInactive).isEmpty();
    }
}


