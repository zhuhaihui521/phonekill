package com.huitop.secondkill.contants;

/**
 * @author zhuhaihui
 * @date 2020-04-14 15:26
 */
public class SecKillContant {
    public static final String ZK_PRODUCTET_SOLD_OUT_FLAG="/product_sold_out_flag";

    public static String getZkSoldOutPath(Long productId){
        return ZK_PRODUCTET_SOLD_OUT_FLAG+"/"+productId;
    }

}
