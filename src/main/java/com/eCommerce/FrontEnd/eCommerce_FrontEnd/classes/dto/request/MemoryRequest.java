package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request;

public class MemoryRequest extends ProductRequest{
    private Integer id;
    private String errorMSG;
    private Boolean contained;

    public Boolean getContained() {
        return contained;
    }

    public void setContained(Boolean contained) {
        this.contained = contained;
    }

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
