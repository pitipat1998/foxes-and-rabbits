package io.muzoo.ooc.ecosystems.animal;

import io.muzoo.ooc.ecosystems.Field;
import io.muzoo.ooc.ecosystems.Location;

import java.util.List;

public abstract class Animal {

    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's position
    private Location location;

    /**
     * Create a new animal. A animal may be created with age
     * zero (a new born) or with a random age.
     *
     * @param randomAge If true, the rabbit will have a random age.
     */
    protected Animal(){
        alive = true;
    }

    /**
     * set whether the animal is alive or not.
     * @param alive is animal alive or not
     */
    protected void setAlive(boolean alive) {
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

    public abstract void act(Field currentField, Field updatedField, List newAnimals);
}
