package com.example.pixShift.service.product;

import com.example.pixShift.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    public Product saveProduct(Product product);

    public List<Product> getAllProducts();

    public Optional<Product> getProductById(Long id);

    public void deleteProducts(List<Long> productIds);
    void updateProductWithCompressedImageUrl(String publicUrl, Product product);
}
