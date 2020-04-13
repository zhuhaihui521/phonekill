package com.huitop.secondkill.mapper;

import com.huitop.secondkill.entity.Order;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {
    @Insert("insert into seckillorder(id,productid,amount) values(#{id},#{productid},#{amount})")
    void save(Order order);

    @Delete("delete from seckillorder where id=#{id}")
    void deleteOrder(@Param("id") Long id);
}
