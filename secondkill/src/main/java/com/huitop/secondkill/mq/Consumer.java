package com.huitop.secondkill.mq;


import com.huitop.secondkill.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "miaoshatest")
public class Consumer {
      @Autowired
      private OrderService orderService;
      @RabbitHandler
      public void process(@Payload Long productId){
          orderService.seckill(productId);
      }
}
