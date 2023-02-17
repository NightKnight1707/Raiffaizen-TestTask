package org.example.utils;

public class CarAnswer {
    private  String color;
    private Integer horsepower;
    private  Double tax;


    public CarAnswer(String color, Double tax, Integer horsepower) {
        this.color = color;
        this.horsepower = horsepower;
        this.tax = tax;
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
