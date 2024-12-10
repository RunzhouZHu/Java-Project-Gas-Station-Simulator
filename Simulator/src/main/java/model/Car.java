package model;

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
    private double step = 5;

    /**
     * Constructs a Car with the specified position, dimensions, and icon.
     *
     * @param x the initial x-coordinate of the car
     * @param y the initial y-coordinate of the car
     * @param width the width of the car
     * @param height the height of the car
     * @param icon the icon representing the car
     */
    public Car(double x, double y, double width, double height, String icon) {
        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        this.moving = false;
        this.width = width;
        this.height = height;
        this.icon = icon;
        this.direction = "right";
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

        if (Math.abs(dx) <= step && Math.abs(dy) <= step) {
            moving = false;
            x = targetX;
            y = targetY;
            return;
        } 

        if (y > 415 && Math.abs(dx) > 0) {
            moveUp();
        } else if (y < 415 && Math.abs(dx) > 0) {
            moveDown();
        } 

        if (dx > 0) {
            setDirection("right");
            moveRight();
        } else if (dx < 0) {
            setDirection("left");
            moveLeft();
        } else if (dy != 0) {
            if (dy > 0) {
                setDirection("down");
                moveDown();
            } else {
                setDirection("up");
                moveUp();
            }
        }
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
        y -= step;
    }

    /**
     * Moves the car down by the step size.
     */
    public void moveDown() {
        y += step;
    }

    /**
     * Moves the car left by the step size.
     */
    public void moveLeft() {
        x -= step;
    }

    /**
     * Moves the car right by the step size.
     */
    public void moveRight() {
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
