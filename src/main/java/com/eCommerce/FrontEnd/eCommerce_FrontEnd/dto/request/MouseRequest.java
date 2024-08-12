package com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.request;

public class MouseRequest {
    private Integer id;
    private String errorMSG;

    public String getErrorMSG() {
        return errorMSG;
    }

    public void setErrorMSG(String errorMSG) {
        this.errorMSG = errorMSG;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
