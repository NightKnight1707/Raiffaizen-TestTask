package org.example.utils;

public class CarInput {

    private String color;
    private Integer horsepower;

    private Integer quantity;

    public CarInput(String color, Integer horsepower, Integer quantity) {
        this.color = color;
        this.horsepower = horsepower;
        this.quantity = quantity;
    }

    public String getColor() {
        return color;
    }

    public Integer getHorsepower() {
        return horsepower;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setHorsepower(Integer horsepower) {
        this.horsepower = horsepower;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
