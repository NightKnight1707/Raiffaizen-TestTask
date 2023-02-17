package org.example.utils;

public class CarWithQuantity {
    private  String color;
    private Integer horsepower;
    private  Double tax;

    private Integer quantity;

    public CarWithQuantity(String color, Double tax, Integer horsepower, Integer quantity) {
        this.color = color;
        this.horsepower = horsepower;
        this.tax = tax;
        this.quantity = quantity;
    }

    public String getColor() {
        return color;
    }

    public Double getTax() {
        return tax;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Integer getHorsepower() {
        return horsepower;
    }

    public void setHorsepower(Integer horsepower) {
        this.horsepower = horsepower;
    }

}
