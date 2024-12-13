package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {
    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
    }

    @Test
    void testInitialPosition() {
        assertEquals(20, car.getX(), "Initial x-coordinate should be 20");
        assertEquals(326, car.getY(), "Initial y-coordinate should be 326");
    }

    @Test
    void testSetSource() {
        car.setSource(100, 200);
        assertEquals(100, car.getX(), "x-coordinate should be set to 100");
        assertEquals(200, car.getY(), "y-coordinate should be set to 200");
    }

    @Test
    void testSetTarget() {
        car.setTarget(300, 400);
        assertTrue(car.getMoving(), "Car should be moving after setting target");
    }

    @Test
    void testMove() {
        car.setTarget(30, 326);
        car.move();
        assertEquals(30, car.getX(), "x-coordinate should be 30 after moving");
        assertEquals(326, car.getY(), "y-coordinate should be 326 after moving");
        assertFalse(car.getMoving(), "Car should stop moving after reaching target");
    }

    @Test
    void testStop() {
        car.setTarget(300, 400);
        car.stop();
        assertFalse(car.getMoving(), "Car should stop moving");
    }

    @Test
    void testMoveUp() {
        car.moveUp();
        assertEquals(316, car.getY(), "y-coordinate should decrease by step size");
    }

    @Test
    void testMoveDown() {
        car.moveDown();
        assertEquals(336, car.getY(), "y-coordinate should increase by step size");
    }

    @Test
    void testMoveLeft() {
        car.moveLeft();
        assertEquals(10, car.getX(), "x-coordinate should decrease by step size");
    }

    @Test
    void testMoveRight() {
        car.moveRight();
        assertEquals(30, car.getX(), "x-coordinate should increase by step size");
    }

    @Test
    void testGetIcon() {
        String icon = car.getIcon();
        assertTrue(icon.contains("images/"), "Icon should contain 'images/'");
    }

    @Test
    void testGetWidthAndHeight() {
        car.setDirection("up");
        assertEquals(26, car.getWidth(), "Width should be 26 when direction is up");
        assertEquals(53, car.getHeight(), "Height should be 53 when direction is up");

        car.setDirection("left");
        assertEquals(53, car.getWidth(), "Width should be 53 when direction is left");
        assertEquals(26, car.getHeight(), "Height should be 26 when direction is left");
    }
}