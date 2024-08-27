package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request;

public class PsuRequest extends ProductRequest{
    private Integer id;
    private Boolean cart;
    private Boolean contained;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getContained() {
        return contained;
    }
    public void setContained(Boolean contained) {
        this.contained = contained;
    }
    public Boolean getCart() {
        return cart;
    }
    public void setCart(Boolean cart) {
        this.cart = cart;
    }
}
