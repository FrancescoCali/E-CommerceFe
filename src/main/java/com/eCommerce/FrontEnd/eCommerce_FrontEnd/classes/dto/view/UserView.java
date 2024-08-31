package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view;

public class UserView {
    private Integer id;
    private String username;
    private String password;
    private String role;
    private String  Address;
    public String getAddress(){return Address;}

    public void setAddress(String Address) {
        this.Address=Address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
