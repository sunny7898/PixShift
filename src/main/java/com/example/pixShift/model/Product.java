package com.example.pixShift.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "products")
@Getter
@Setter
public class Product {
    @Id
    private Long id;
    private String productName;
    private List<String> inputImageUrls;
    private List<String> outputImageUrls;
}
