package controller;


import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import model.Car;

/**
 * The CarController class manages the car's movement and state in the simulation.
 * It initializes a car from a predefined list and provides methods to control the car's direction and movement.
 */
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

    /**
     * Constructs a CarController and initializes a car from the predefined list.
     * The car's initial position and target are set to (20, 415).
     */
    public CarController() {
        Random random = new Random();
        this.car = cars.get(random.nextInt(cars.size()));
        this.car.setX(20);
        this.car.setY(415);
        this.car.setTarget(20, 415);
    }
    /**
     * Sets the target position for the car.
     *
     * @param x the target x-coordinate
     * @param y the target y-coordinate
     */
    public void setCarTarget(double x, double y) {
        car.setTarget(x, y);
    }

    /**
     * Moves the car towards its target position.
     */
    public void carMove() {
        car.move();
    }

    /**
     * Stops the car's movement.
     */
    public void carStop() {
        car.stop();
    }

    /**
     * Turns the car to face upwards.
     */
    public void turnUp() {
        car.setDirection("up");
    }

    /**
     * Turns the car to face right.
     */
    public void turnRight() {
        car.setDirection("right");
    }

    /**
     * Turns the car to face left.
     */
    public void turnLeft() {
        car.setDirection("left");
    }

    /**
     * Turns the car to face downwards.
     */
    public void turnDown() {
        car.setDirection("down");
    }


    /**
     * Returns the car's current x-coordinate.
     *
     * @return the car's x-coordinate
     */
    public double getCarX() {
        return car.getX();
    }

    /**
     * Returns the car's current y-coordinate.
     *
     * @return the car's y-coordinate
     */
    public double getCarY() {
        return car.getY();
    }

    /**
     * Returns the file path of the car's icon.
     *
     * @return the car's icon file path
     */
    public String getCarIcon() {
        return car.getIcon();
    }


    /**
     * Returns the car's width.
     *
     * @return the car's width
     */
    public double getCarWidth() {
        return car.getWidth();
    }

    /**
     * Returns the car's height.
     *
     * @return the car's height
     */
    public double getCarHeight() {
        return car.getHeight();
    }
}
