package com.huitop.secondkill.service;

import com.huitop.secondkill.entity.Product;
import com.huitop.secondkill.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    public void secill(Long productId) {

    }

    public Product getProductById(Long productId) {
         return productMapper.getProductById(productId);
    }

    public int deductProductStock(Long productId) {
        return productMapper.deductProductStock(productId);
    }
    public List<Product> getProductList(){
        return productMapper.getProductList();
    }
}
