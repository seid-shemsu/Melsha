package com.izhar.melsha.models;

import java.io.Serializable;

public class ItemModel implements Serializable {
    String id, code, name, type, model, size;
    int quantity, avg_price, total_capital, jemmo, dessie, kore;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getJemmo() {
        return jemmo;
    }

    public void setJemmo(int jemmo) {
        this.jemmo = jemmo;
    }

    public int getDessie() {
        return dessie;
    }

    public void setDessie(int dessie) {
        this.dessie = dessie;
    }

    public int getKore() {
        return kore;
    }

    public void setKore(int kore) {
        this.kore = kore;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getAvg_price() {
        return avg_price;
    }

    public void setAvg_price(int avg_price) {
        this.avg_price = avg_price;
    }

    public int getTotal_capital() {
        return total_capital;
    }

    public void setTotal_capital(int total_capital) {
        this.total_capital = total_capital;
    }
}
