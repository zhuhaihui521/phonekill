package com.huitop.secondkill.controller;


import com.huitop.secondkill.common.Result;
import com.huitop.secondkill.entity.Product;
import com.huitop.secondkill.service.OrderService;
import com.huitop.secondkill.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/seckill")
public class SeckillController {
    private static final Logger logger= LoggerFactory.getLogger(SeckillController.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ProductService productService;

    public final static ConcurrentMap<Long,Boolean> map=new ConcurrentHashMap<Long,Boolean>();
    @PostConstruct
    public void initStock(){
        List<Product> products = productService.getProductList();
        for (Product product:products){
            stringRedisTemplate.opsForValue().set(String.valueOf(product.getId()),product.getStock()+"",300l, TimeUnit.SECONDS);
            map.put(product.getId(),true);
        }
    }

    @PostMapping("/{productId}")
    public Result<String> seckill(@PathVariable("productId") Long productId){
        try{
            if (!map.get(productId)){
                return Result.ofFail(9999,"商品已售完");
            }
            Long decrement = stringRedisTemplate.opsForValue().decrement(productId+"");
            if (decrement < 0){
                stringRedisTemplate.opsForValue().increment(productId+"");
                map.put(productId,false);
                return Result.ofFail(9999,"商品已售完");
            }
            orderService.seckill(productId);
        }catch (Exception e){
            logger.error("创建订单失败",e);
            stringRedisTemplate.opsForValue().increment(productId+"");
            return Result.ofFail(9999,"创建订单失败");
        }
        return Result.ofSuccess("秒杀成功");
    }
}
