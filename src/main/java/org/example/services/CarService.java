package org.example.services;

import org.example.models.Car;
import org.example.utils.CarAnswer;
import org.example.utils.CarWithQuantity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;

@Component
public class CarService {

    private static final String URL = "jdbc:postgresql://localhost:5432/Raiffaizen+Test";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "170701";

    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<CarAnswer> getCars(String color, String operation, Integer horsepower) throws SQLException {

        ArrayList<CarAnswer> cars = null;
        try {
            PreparedStatement preparedStatement;

            cars = new ArrayList<>();

            ResultSet result = null;
            switch (operation) {
                case "moreThan":
                    preparedStatement = connection.prepareStatement("SELECT horse_power,SUM(quantity) FROM CAR " +
                            "WHERE (color = ?) AND (horse_power > ?)" +
                            "GROUP BY horse_power",ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
                    preparedStatement.setString(1, color);
                    preparedStatement.setInt(2, horsepower);
                    result = preparedStatement.executeQuery();
                    System.out.println(result);
                    break;
                case "lessThan":
                    preparedStatement = connection.prepareStatement("SELECT horse_power,SUM(quantity) FROM CAR " +
                            "WHERE (color = ?) AND (horse_power < ?)" +
                            "GROUP BY horse_power",ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
                    preparedStatement.setString(1, color);
                    preparedStatement.setInt(2, horsepower);
                    result = preparedStatement.executeQuery();
                    break;
                case "equal":
                    preparedStatement = connection.prepareStatement("SELECT horse_power,SUM(quantity) FROM CAR " +
                            "WHERE (color = ?) AND (horse_power = ?)" +
                            "GROUP BY horse_power",ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
                    preparedStatement.setString(1, color);
                    preparedStatement.setInt(2, horsepower);
                    result = preparedStatement.executeQuery();
                    break;
                default:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверный формат данных");
            }
            if (!result.next()) {
                return null;
            } else {

                result.previous();
                while (result.next()) {
                    double koef = getKoef(result.getInt("horse_power"));

                    double tax = result.getInt("horse_power") * koef;
                    Integer horses = result.getInt("horse_power");

                    CarAnswer carAnswer = new CarAnswer(color, tax, horses);

                    cars.add(carAnswer);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }

    public CarWithQuantity addCar(Car car, Integer quantity) throws SQLException {

        try{
            PreparedStatement checkStatement = connection.prepareStatement("SELECT color,horse_power from Car WHERE color = ? AND horse_power = ?");
            checkStatement.setString(1,car.getColor());
            checkStatement.setInt(2,car.getHorsepower());
            ResultSet temp = checkStatement.executeQuery();
            PreparedStatement preparedStatement;
            if(!temp.next()){
                preparedStatement =
                        connection.prepareStatement("INSERT INTO Car(color,horse_power,quantity) " +
                                        "VALUES (?,?,?)"
                                        ,ResultSet.TYPE_SCROLL_INSENSITIVE,
                                ResultSet.CONCUR_UPDATABLE);
                preparedStatement.setString(1,car.getColor());
                preparedStatement.setInt(2,car.getHorsepower());
                preparedStatement.setInt(3,quantity);
            }else{
                preparedStatement =
                        connection.prepareStatement("UPDATE Car " +
                                        "SET quantity = quantity + ? " +
                                        "WHERE (color = ?) AND (horse_power = ?)",ResultSet.TYPE_SCROLL_INSENSITIVE,
                                ResultSet.CONCUR_UPDATABLE);
                preparedStatement.setInt(1,quantity);
                preparedStatement.setString(2,car.getColor());
                preparedStatement.setInt(3,car.getHorsepower());
            }
            preparedStatement.executeUpdate();


        }catch (SQLException e){
            e.printStackTrace();
        }

        double koef = getKoef(car.getHorsepower());
        CarWithQuantity answerCar = new CarWithQuantity(car.getColor(),koef * car.getHorsepower() * quantity, car.getHorsepower(),quantity);

        return  answerCar;
    }

    public CarWithQuantity sellCar(Car car, Integer quantity) throws SQLException {

        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE Car " +
                                    "SET quantity = quantity - ? " +
                                    "WHERE (color = ?) AND (horse_power = ?)",ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);

            preparedStatement.setInt(1,quantity);
            preparedStatement.setString(2,car.getColor());
            preparedStatement.setInt(3,car.getHorsepower());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        double koef = getKoef(car.getHorsepower());
        CarWithQuantity answerCar = new CarWithQuantity(car.getColor(),koef * car.getHorsepower() * quantity, car.getHorsepower(),quantity);
        return  answerCar;
    }

    public double getKoef(Integer horses){
        if (horses <= 100) {
            return  2.5;
        } else if (horses <= 150) {
            return  3.5;
        } else if (horses <= 200) {
            return  5;
        } else if (horses <= 250) {
            return  7.5;
        } else {
            return  15;
        }
    }

}
