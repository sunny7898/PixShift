package com.example.pixShift.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.pixShift.model.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, Long> {
}
