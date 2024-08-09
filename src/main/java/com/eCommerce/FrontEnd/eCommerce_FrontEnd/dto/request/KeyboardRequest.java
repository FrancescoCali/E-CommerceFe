package com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.request;

public class KeyboardRequest {
    private Integer id;
    private Integer idProdotto;

    public Integer getIdProdotto()
    {
        return idProdotto;
    }

    public void setIdProdotto(Integer idProdotto)
    {
        this.idProdotto=idProdotto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
