package com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.view;

public class CartView extends ProductView{
    private Integer id;
    private Integer idUser;

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
