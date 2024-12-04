package controller;


import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import model.Car;


public class CarController {
    private Car car;
    static List<Car> cars = new ArrayList<>();

    static {
        cars.add(new Car(0, 0, 26, 52, "images/black.png"));
        cars.add(new Car(0, 0, 26, 50, "images/blue.png"));
        cars.add(new Car(0, 0, 26, 52, "images/red.png"));
        cars.add(new Car(0, 0, 26, 58, "images/yellow.png"));
        cars.add(new Car(0, 0, 26, 55, "images/white.png"));
    }

    public CarController() {
        Random random = new Random();
        this.car = cars.get(random.nextInt(cars.size()));
        this.car.setX(20);
        this.car.setY(415);
        this.car.setTarget(20, 415);
    }

    public void setCarTarget(double x, double y) {
        car.setTarget(x, y);
    }

    public void carMove() {
        car.move();
    }

    public void carStop() {
        car.stop();
    }

    public void turnUp() {
        car.setDirection("up");
    }

    public void turnRight() {
        car.setDirection("right");
    }

    public void turnLeft() {
        car.setDirection("left");
    }

    public void turnDown() {
        car.setDirection("down");
    }

    public double getCarX() {
        return car.getX();
    }

    public double getCarY() {
        return car.getY();
    }

    public String getCarIcon() {
        return car.getIcon();
    }

    public double getCarWidth() {
        return car.getWidth();
    }

    public double getCarHeight() {
        return car.getHeight();
    }
}
