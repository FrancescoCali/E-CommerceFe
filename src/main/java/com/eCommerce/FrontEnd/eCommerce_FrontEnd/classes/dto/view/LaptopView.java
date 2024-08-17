package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view;

public class LaptopView extends ProductView {

    private Integer id;

    private Integer idRAM;
    private String RAM;

    private Integer idCPU;
    private String CPU;

    private Integer idGPU;
    private String GPU;

    private Integer idMemory;
    private String Memory;

    private Integer idMotherboard;
    private String Motherboard;

    private Integer idPSU ;
    private String PSU ;

    private Integer idCooler ;
    private String cooler ;

    private Boolean cart ;

    public Integer getIdRAM() {
        return idRAM;
    }

    public void setIdRAM(Integer idRAM) {
        this.idRAM = idRAM;
    }

    public String getRAM() {
        return RAM;
    }

    public void setRAM(String RAM) {
        this.RAM = RAM;
    }

    public String getCooler() {
        return cooler;
    }

    public void setCooler(String cooler) {
        this.cooler = cooler;
    }

    public Integer getIdPSU() {
        return idPSU;
    }

    public void setIdPSU(Integer idPSU) {
        this.idPSU = idPSU;
    }

    public String getPSU() {
        return PSU;
    }

    public void setPSU(String PSU) {
        this.PSU = PSU;
    }

    public Integer getIdCooler() {
        return idCooler;
    }

    public void setIdCooler(Integer idCooler) {
        this.idCooler = idCooler;
    }

    public String getCPU() {
        return CPU;
    }

    public void setCPU(String CPU) {
        this.CPU = CPU;
    }

    public String getGPU() {
        return GPU;
    }

    public void setGPU(String GPU) {
        this.GPU = GPU;
    }

    public String getMemory() {
        return Memory;
    }

    public void setMemory(String memory) {
        Memory = memory;
    }

    public String getMotherboard() {
        return Motherboard;
    }

    public void setMotherboard(String motherboard) {
        Motherboard = motherboard;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCPU() {
        return idCPU;
    }

    public void setIdCPU(Integer idCPU) {
        this.idCPU = idCPU;
    }

    public Integer getIdGPU() {
        return idGPU;
    }

    public void setIdGPU(Integer idGPU) {
        this.idGPU = idGPU;
    }

    public Integer getIdMemory() {
        return idMemory;
    }

    public void setIdMemory(Integer idMemory) {
        this.idMemory = idMemory;
    }

    public Integer getIdMotherboard() {
        return idMotherboard;
    }

    public void setIdMotherboard(Integer idMotherboard) {
        this.idMotherboard = idMotherboard;
    }

    public Boolean getCart() {
        return cart;
    }

    public void setCart(Boolean cart) {
        this.cart = cart;
    }

}
