package io.muzoo.ooc.ecosystems.animal;

import io.muzoo.ooc.ecosystems.Field;
import io.muzoo.ooc.ecosystems.Location;

import java.util.List;

public abstract class Animal {

    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's age.
    private int age;
    // The animal's position
    private Location location;

    public void initialize(boolean randomAge) {
        setAlive(true);
    }
    /**
     * set whether the animal is alive or not.
     * @param alive is animal alive or not
     */
    void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Check whether the animal is alive or not.
     *
     * @return true if the rabbit is still alive.
     */
    public boolean isAlive() {
        return alive;
    }

    protected int getAge() { return age; }

    protected void setAge(int age) { this.age = age; }

    protected abstract void incrementAge();

    /**
     * get location of the animal
     *
     * @return location of the animal
     */
    protected Location getLocation() {
        return location;
    }

    /**
     * Set the animal's location.
     *
     * @param row The vertical coordinate of the location.
     * @param col The horizontal coordinate of the location.
     */
    public void setLocation(int row, int col) {
        this.location = new Location(row, col);
    }

    /**
     * Set the rabbit's location.
     *
     * @param location The rabbit's location.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    public abstract void act(Field currentField, Field updatedField, List<Animal> newAnimals);

    /**
     * A animal can breed if it has reached the breeding age.
     */
    protected abstract boolean canBreed();

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     *
     * @return The number of births (may be zero).
     */
    protected abstract int breed();
}
