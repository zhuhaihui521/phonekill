package com.huitop.secondkill.zookeeper;

import com.huitop.secondkill.contants.SecKillContant;
import com.huitop.secondkill.controller.SeckillController;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * @author zhuhaihui
 * @date 2020-04-14 15:21
 */
@Component
public class ZookeeperWatcher implements Watcher {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperWatcher.class);
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    private ZooKeeper zooKeeper;
    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
            if(watchedEvent.getType() == Event.EventType.NodeDataChanged){
               //zk节点发生变化
               String path=watchedEvent.getPath();
               try {
                   String soldOutFlag=new String(zooKeeper.getData(path,true,new Stat()));
                   logger.info("zk数据节点修改变动,path={},value={}",path,soldOutFlag);
                   if ("false".equals(soldOutFlag)){
                       String productId=path.substring(path.lastIndexOf("/")+1,path.length());
                       SeckillController.getProductSoldOutMap().remove(productId);
                       logger.info("清楚商品内存售完标记");
                   }
               } catch (KeeperException e) {
                   e.printStackTrace();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }

           }
    }

}
