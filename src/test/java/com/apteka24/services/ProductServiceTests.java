package com.apteka24.services;

import com.apteka24.models.Product;
import com.apteka24.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product activeProduct;
    private Product inactiveProduct;

    @BeforeEach
    void setUp() {
        activeProduct = new Product();
        activeProduct.setId(1L);
        activeProduct.setName("Aspirin");
        activeProduct.setCategory("Painkiller");
        activeProduct.setActive(true);
        activeProduct.setPrice(new BigDecimal("9.99"));

        inactiveProduct = new Product();
        inactiveProduct.setId(2L);
        inactiveProduct.setName("Old");
        inactiveProduct.setCategory("Legacy");
        inactiveProduct.setActive(false);
    }

    @Test
    void getAllProducts_returnsOnlyActive() {
        when(productRepository.findByActiveTrue()).thenReturn(List.of(activeProduct));

        List<Product> products = productService.getAllProducts();

        assertThat(products).containsExactly(activeProduct);
        verify(productRepository).findByActiveTrue();
    }

    @Test
    void getProductsByCategory_filtersByCategoryAndActive() {
        when(productRepository.findByCategoryAndActiveTrue("Painkiller"))
                .thenReturn(List.of(activeProduct));

        List<Product> products = productService.getProductsByCategory("Painkiller");

        assertThat(products).containsExactly(activeProduct);
        verify(productRepository).findByCategoryAndActiveTrue("Painkiller");
    }

    @Test
    void searchProducts_searchesByNameAndActive() {
        when(productRepository.findByNameContainingIgnoreCaseAndActiveTrue("asp"))
                .thenReturn(List.of(activeProduct));

        List<Product> products = productService.searchProducts("asp");

        assertThat(products).containsExactly(activeProduct);
        verify(productRepository).findByNameContainingIgnoreCaseAndActiveTrue("asp");
    }

    @Test
    void getCategories_returnsDistinctCategories() {
        when(productRepository.findDistinctCategories()).thenReturn(List.of("Painkiller", "Vitamins"));

        List<String> categories = productService.getCategories();

        assertThat(categories).containsExactly("Painkiller", "Vitamins");
        verify(productRepository).findDistinctCategories();
    }

    @Test
    void getProductById_returnsActiveOptional() {
        when(productRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(activeProduct));

        Optional<Product> found = productService.getProductById(1L);

        assertThat(found).contains(activeProduct);
        verify(productRepository).findByIdAndActiveTrue(1L);
    }

    @Test
    void saveProduct_persists() {
        when(productRepository.save(activeProduct)).thenReturn(activeProduct);

        Product saved = productService.saveProduct(activeProduct);

        assertThat(saved).isEqualTo(activeProduct);
        verify(productRepository).save(activeProduct);
    }

    @Test
    void deleteProduct_softDeletesWhenExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        productService.deleteProduct(1L);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        assertThat(captor.getValue().getActive()).isFalse();
    }
}


