package com.monk.couponsystem.services;

import com.monk.couponsystem.models.Product;
import com.monk.couponsystem.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}

