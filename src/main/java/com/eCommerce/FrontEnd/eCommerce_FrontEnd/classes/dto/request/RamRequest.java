package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request;

public class RamRequest extends ProductRequest{
    private Integer id;
    private Integer idProduct;
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

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
