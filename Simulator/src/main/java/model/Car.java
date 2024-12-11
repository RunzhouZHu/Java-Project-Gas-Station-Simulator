package model;

import java.util.Random;

/**
 * Represents a car in the simulation.
 * The car can move to a target position and change direction.
 */
public class Car {
    private double x;
    private double y;
    private double targetX;
    private double targetY;
    private boolean moving;
    private double width;
    private double height;
    private String icon;
    private String direction;
    private double step = 10;
    static String[] icons = {
        "images/black.png",
        "images/blue.png",
        "images/red.png",
        "images/yellow.png",
        "images/white.png"
    };

    /**
     * Constructs a Car with the specified position, dimensions, and icon.
     *
     * @param x the initial x-coordinate of the car
     * @param y the initial y-coordinate of the car
     * @param width the width of the car
     * @param height the height of the car
     * @param icon the icon representing the car
     */
    public Car() {
        this.x = 20;
        this.y = 326;
        this.targetX = 20;
        this.targetY = 326;
        this.moving = false;
        this.width = 26;
        this.height = 53;
        this.icon = "images/red.png";
        //Random random = new Random();
        //this.icon = icons[random.nextInt(icons.length)];
        this.direction = "right";
    }

    /**
     * Sets the source position for the car to move from.
     *
     * @param x the source x-coordinate
     * @param y the source y-coordinate
     */
    public void setSource(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the target position for the car to move to.
     *
     * @param x the target x-coordinate
     * @param y the target y-coordinate
     */
    public void setTarget(double x, double y) {
        targetX = x;
        targetY = y;
        moving = true;
    }

    /**
     * Moves the car towards the target position.
     */
    public void move() {
        if (!moving) return;

        double dx = targetX - x;
        double dy = targetY - y;

        if (Math.abs(dx) <= step) {
            x = targetX;
        }
        if (Math.abs(dy) <= step) {
            y = targetY;
        } 
        if (x == targetX && y == targetY) {
            moving = false;
            return;
        }

        if (dx > 0) {
            moveRight();
        }
        if (dx < 0) {
            moveLeft();
        }
        if (dy > 0) {
            moveDown();
        }
        if (dy < 0) {
            moveUp();
        }
    }

    public boolean getMoving() {
        return moving;
    }

    /**
     * Stops the car from moving.
     */
    public void stop() {
        moving = false;
    }

    /**
     * Moves the car up by the step size.
     */
    public void moveUp() {
        setDirection("up");
        y -= step;
    }

    /**
     * Moves the car down by the step size.
     */
    public void moveDown() {
        setDirection("down");
        y += step;
    }

    /**
     * Moves the car left by the step size.
     */
    public void moveLeft() {
        setDirection("left");
        x -= step;
    }

    /**
     * Moves the car right by the step size.
     */
    public void moveRight() {
        setDirection("right");
        x += step;
    }

    /**
     * Sets the x-coordinate of the car.
     *
     * @param x the new x-coordinate
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of the car.
     *
     * @param y the new y-coordinate
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Sets the direction of the car.
     *
     * @param direction the new direction ("up", "down", "left", "right")
     */
    public void setDirection(String direction) {
        this.direction = direction;
        moving = true;
    }

    /**
     * Returns the x-coordinate of the car.
     *
     * @return the x-coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the car.
     *
     * @return the y-coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the icon of the car, adjusted for the current direction.
     *
     * @return the icon
     */
    public String getIcon() {
        String[] parts = icon.split("\\.");
        String newIcon = parts[0] + "_" + direction + "." + parts[1];
        return newIcon;
    }

    /**
     * Returns the width of the car, adjusted for the current direction.
     *
     * @return the width
     */
    public double getWidth() {
        if (direction.equals("up") || direction.equals("down")) {
            return width;
        }
        return height; 
    }

    /**
     * Returns the height of the car, adjusted for the current direction.
     *
     * @return the height
     */
    public double getHeight() {
        if (direction.equals("up") || direction.equals("down")) {
            return height;
        }
        return width;
    }
}
