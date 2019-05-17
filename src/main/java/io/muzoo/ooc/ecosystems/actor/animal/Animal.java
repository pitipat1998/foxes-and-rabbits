package io.muzoo.ooc.ecosystems.actor.animal;

import io.muzoo.ooc.ecosystems.actor.Actor;
import io.muzoo.ooc.ecosystems.Location;

import java.util.Random;

public abstract class Animal implements Actor {

    // The age at which a animal can start to breed.
    private int breedingAge;
    // The age to which a animal can live.
    private int maxAge;
    // The likelihood of a animal breeding.
    private double breedingProbability;
    // The maximum number of births.
    private int maxLitterSize;
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's age.
    private int age;
    // The animal's position
    private Location location;

    private boolean randomAge;
    // A shared random number generator to control breeding.
    protected static final Random rand = new Random();

    protected Animal(boolean randomAge){
        this.alive = true;
        this.randomAge = randomAge;
    }

    public int getBreedingAge() { return breedingAge; }

    public void setBreedingAge(int breedingAge) { this.breedingAge = breedingAge; }

    public int getMaxAge() { return maxAge; }

    public void setMaxAge(int maxAge) { this.maxAge = maxAge; }

    public double getBreedingProbability() { return breedingProbability; }

    public void setBreedingProbability(double breedingProbability) { this.breedingProbability = breedingProbability; }

    public int getMaxLitterSize() { return maxLitterSize; }

    public void setMaxLitterSize(int maxLitterSize) { this.maxLitterSize = maxLitterSize; }

    public void setRandomAge(boolean randomAge) { this.randomAge = randomAge; }

    public boolean isRandomAge() { return randomAge; }

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

    /**
     * Tell the rabbit that it's dead now :(
     */
    public void setDead() {
        setAlive(false);
    }

    @Override
    public boolean isActive() {
        return isAlive();
    }

    @Override
    public void setActive(boolean active){
        setAlive(active);
    }

    protected int getAge() { return age; }

    protected void setAge(int age) { this.age = age; }

    protected abstract void initialize();

    /**
     * Increase the age.
     * This could result in the rabbit's death.
     */
    protected void incrementAge() {
        setAge(getAge()+1);
        if (getAge() > getMaxAge()) {
            setAlive(false);
        }
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

    /**
     * A rabbit can breed if it has reached the breeding age.
     *
     * @return true if the rabbit can breed, false otherwise.
     */
    protected boolean canBreed() {
        return getAge() >= getBreedingAge();
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     *
     * @return The number of births (may be zero).
     */
    protected int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }

    public abstract Animal clone();

    public abstract static class AnimalBuilder<T extends AnimalBuilder<T>> {
        private Integer breedingAge;
        private Integer maxAge;
        private Double breedingProbability;
        private Integer maxLitterSize;
        private boolean randomAge;

        public Integer getBreedingAge() { return breedingAge; }

        public Integer getMaxAge() { return maxAge; }

        public Double getBreedingProbability() { return breedingProbability; }

        public Integer getMaxLitterSize() { return maxLitterSize; }

        public boolean isRandomAge() { return randomAge; }

        public AnimalBuilder(boolean randomAge){
            this.randomAge = randomAge;
        }

        public T breedingAge(int value){
            this.breedingAge = value;
            return self();
        }

        public T maxAge(int value){
            this.maxAge = value;
            return self();
        }

        public T breedingProbability(double value){
            this.breedingProbability = value;
            return self();
        }

        public T maxLitterSize(int value){
            this.maxLitterSize = value;
            return self();
        }

        public T randomAge(boolean value){
            this.randomAge = value;
            return self();
        }

        protected abstract T self();

    }

}
