package com.example.pixShift.service.product;

import com.example.pixShift.model.Product;
import com.example.pixShift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    @Override
    public void deleteProducts(List<Long> productIds) {
        productRepository.deleteAllById(productIds);
    }
    @Override
    public void updateProductWithCompressedImageUrl(String publicUrl, Product product) {
        List<String> existingOutputUrls = product.getOutputImageUrls();
        if (existingOutputUrls == null) {
            existingOutputUrls = new ArrayList<>();
        }
        existingOutputUrls.add(publicUrl);
        product.setOutputImageUrls(existingOutputUrls);
        productRepository.save(product);
    }
}
