package org.example.models;

public class Car {
    private String color;
    private Integer horsepower;

    public Car(String color, Integer horsepower){
        this.color = color;
        this.horsepower = horsepower;
    }

    public String getColor() {
        return color;
    }

    public Integer getHorsepower() {
        return horsepower;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setHorsepower(Integer horsepower) {
        this.horsepower = horsepower;
    }
}
