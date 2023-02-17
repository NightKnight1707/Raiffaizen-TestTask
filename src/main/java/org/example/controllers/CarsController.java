package org.example.controllers;

import org.example.services.CarService;
import org.example.models.Car;
import org.example.utils.CarAnswer;
import org.example.utils.CarInput;
import org.example.utils.CarWithQuantity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class CarsController {

    private final CarService carService;

    public CarsController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/cars")
    public ArrayList<CarAnswer> findCars(@RequestParam("color") @Nullable String color, @RequestParam("operation") @Nullable String operation, @RequestParam("horsepower") @Nullable Integer horsepower, HttpServletResponse response) throws ResponseStatusException {
        if (horsepower == null || color == null || operation == null){
            response.setStatus(400);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Отсутствуют параметры",new IllegalArgumentException());
        }
        if(horsepower <= 0){
            response.setStatus(400);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Неверное значение параметра horsepower",new IllegalArgumentException());
        }
        ArrayList<CarAnswer> result = new ArrayList<>();
        try {
           result = carService.getCars(color, operation, horsepower);
           response.setStatus(200);
        } catch (Exception e) {
            e.getMessage();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ошибка внутри сервера");
        }
        if(result == null){
            //throw new ResponseStatusException(HttpStatus.OK,"Нет записей подходящих критерию");
            response.setStatus(200);
            return new ArrayList<CarAnswer>();
        }
        return result;
    };


    @PostMapping("/cars/income")
    public CarWithQuantity increaseCars(@RequestBody CarInput carInput, HttpServletResponse response) throws ResponseStatusException {
        if (carInput.getColor() == null || carInput.getHorsepower() == null || carInput.getQuantity() == null){
            response.setStatus(400);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Отсутствуют параметры",new IllegalArgumentException());
        }

        Car car = new Car(carInput.getColor(), carInput.getHorsepower());

        CarWithQuantity addedCar = null;
        try {
            addedCar = carService.addCar(car, carInput.getQuantity());
            response.setStatus(200);
        } catch (Exception e) {
            e.getMessage();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ошибка внутри сервера");
        }
        return addedCar;
    }


    @PostMapping("/cars/outcome")
    public CarWithQuantity decreaseCars(@RequestBody CarInput carInput,HttpServletResponse response) throws ResponseStatusException {

        if (carInput.getColor() == null || carInput.getHorsepower() == null || carInput.getQuantity() == null){
            response.setStatus(400);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Отсутствуют параметры",new IllegalArgumentException());
        }

        Car car = new Car(carInput.getColor(),carInput.getHorsepower());

        CarWithQuantity takenCar = null;
        try{
            takenCar = carService.sellCar(car,carInput.getQuantity());
            response.setStatus(200);
        }catch (Exception e){
            e.getMessage();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ошибка внутри сервера");
        }
        return takenCar;
    }

}
