package com.huitop.secondkill.service;

import com.huitop.secondkill.entity.Order;
import com.huitop.secondkill.entity.Product;
import com.huitop.secondkill.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ProductService productService;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Transactional
    public void seckill(Long productId) {
        //查询商品
        Product product = productService.getProductById(productId);
        if (product.getStock() <= 0){
            throw new RuntimeException("商品库存已售完");
        }
        //创建秒杀订单
        Order order=new Order();
        order.setProductid(productId);
        order.setAmount(product.getPrice());
        saveOrder(order);

        //减库存
        int productStock = productService.deductProductStock(productId);
        if (productStock <= 0){
            throw new RuntimeException("商品库存已售完");
        }
    }

    @Transactional
    public void saveOrder(Order order) {
        orderMapper.save(order);
    }
}
