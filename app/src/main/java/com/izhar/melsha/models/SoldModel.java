package com.izhar.melsha.models;

import java.io.Serializable;

public class SoldModel implements Serializable {
    String size, id, code, name, type, model, from_store, date;
    int quantity, purchased_price, sold_price, profit;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public int getPurchased_price() {
        return purchased_price;
    }

    public void setPurchased_price(int purchased_price) {
        this.purchased_price = purchased_price;
    }

    public int getSold_price() {
        return sold_price;
    }

    public void setSold_price(int sold_price) {
        this.sold_price = sold_price;
    }

    public String getFrom_store() {
        return from_store;
    }

    public void setFrom_store(String from_store) {
        this.from_store = from_store;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }
}