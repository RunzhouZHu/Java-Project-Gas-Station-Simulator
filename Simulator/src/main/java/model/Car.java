package model;


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

    public void setTarget(double x, double y) {
        targetX = x;
        targetY = y;
        moving = true;
    }

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

    public void stop() {
        moving = false;
    }

    public void moveUp() {
        y -= step;
    }

    public void moveDown() {
        y += step;
    }

    public void moveLeft() {
        x -= step;
    }

    public void moveRight() {
        x += step;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setDirection(String direction) {
        this.direction = direction;
        moving = true;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getIcon() {
        String[] parts = icon.split("\\.");
        String newIcon = parts[0] + "_" + direction + "." + parts[1];
        return newIcon;
    }

    public double getWidth() {
        if (direction.equals("up") || direction.equals("down")) {
            return width;
        }
        return height; 
    }

    public double getHeight() {
        if (direction.equals("up") || direction.equals("down")) {
            return height;
        }
        return width;
    }
}
