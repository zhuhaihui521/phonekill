package com.huitop.secondkill.entity;


import java.math.BigDecimal;

public class Order {
    private Long id;
    private Long productid;
    private BigDecimal amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Order(Long id, Long productid, BigDecimal amount) {
        this.id = id;
        this.productid = productid;
        this.amount = amount;
    }

    public Order() {
    }
}
