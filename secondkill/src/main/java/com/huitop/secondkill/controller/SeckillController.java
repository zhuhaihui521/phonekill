package com.huitop.secondkill.controller;


import com.huitop.secondkill.common.Result;
import com.huitop.secondkill.contants.SecKillContant;
import com.huitop.secondkill.entity.Product;
import com.huitop.secondkill.mq.Sender;
import com.huitop.secondkill.service.OrderService;
import com.huitop.secondkill.service.ProductService;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private ZooKeeper zooKeeper;
    @Autowired
    private Sender sender;

    public final static ConcurrentMap<Long,Boolean> productSoldOutMap=new ConcurrentHashMap<Long,Boolean>();
    public final static int ZERO = 0;
    public final static int NINENINENIE = 9999;
    public final static String FALSEFlAG = "false";
    public final static String TRUEFlAG = "true";

    public static ConcurrentMap<Long, Boolean> getProductSoldOutMap() {
        return productSoldOutMap;
    }

    @PostConstruct
    public void initStock(){
        List<Product> products = productService.getProductList();
        for (Product product:products){
            stringRedisTemplate.opsForValue().set(String.valueOf(product.getId()),product.getStock()+"",300l, TimeUnit.SECONDS);
        }
    }

    @RequestMapping("/{productId}")
    public Result<String> seckill(@PathVariable("productId") Long productId) throws Exception{
        try{
            if (productSoldOutMap.get(productId) != null){
                return Result.ofFail(NINENINENIE,"商品已售完");
            }
            Long decrement = stringRedisTemplate.opsForValue().decrement(productId+"");
            if (decrement < ZERO){
                productSoldOutMap.put(productId,true);
                stringRedisTemplate.opsForValue().increment(productId+"");
                if(zooKeeper.exists(SecKillContant.getZkSoldOutPath(productId),true) == null){
                    zooKeeper.create(SecKillContant.getZkSoldOutPath(productId),TRUEFlAG.getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
                }
                zooKeeper.exists(SecKillContant.getZkSoldOutPath(productId),true);
                return Result.ofFail(NINENINENIE,"商品已售完");
            }
            sender.send(productId);
        }catch (Exception e){
            logger.error("创建订单失败",e);
            if (productSoldOutMap.get(productId) != null){
                productSoldOutMap.remove(productId);
            }
            stringRedisTemplate.opsForValue().increment(productId+"");
            if (zooKeeper.exists(SecKillContant.getZkSoldOutPath(productId),true) == null){
                zooKeeper.create(SecKillContant.getZkSoldOutPath(productId),FALSEFlAG.getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
                zooKeeper.exists(SecKillContant.getZkSoldOutPath(productId),true);
            }else{
                zooKeeper.setData(SecKillContant.getZkSoldOutPath(productId),FALSEFlAG.getBytes(),-1);
            }
            return Result.ofFail(NINENINENIE,"创建订单失败");
        }
        return Result.ofSuccess("秒杀成功");
    }

}
