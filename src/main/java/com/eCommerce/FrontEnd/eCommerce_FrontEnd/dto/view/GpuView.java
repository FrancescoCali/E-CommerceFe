package com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.view;

public class GpuView extends ProductView{
    private Integer id;
    private Boolean cart ;
    private Boolean contained ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getCart() {
        return cart;
    }

    public void setCart(Boolean cart) {
        this.cart = cart;
    }

    public Boolean getContained() {
        return contained;
    }

    public void setContained(Boolean contained) {
        this.contained = contained;
    }
}
