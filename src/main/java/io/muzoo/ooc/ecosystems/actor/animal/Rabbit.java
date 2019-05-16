package io.muzoo.ooc.ecosystems.actor.animal;

import io.muzoo.ooc.ecosystems.actor.Actor;
import io.muzoo.ooc.ecosystems.Field;
import io.muzoo.ooc.ecosystems.Location;

import java.util.List;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2002.10.28
 */
public class Rabbit extends Animal {
    // Characteristics shared by all rabbits (static fields).

    // The age at which a rabbit can start to breed.
    private int breedingAge = 5;
    // The age to which a rabbit can live.
    private int maxAge = 50;
    // The likelihood of a rabbit breeding.
    private double breedingProbability = 0.15;
    // The maximum number of births.
    private int maxLitterSize = 5;
    // A shared random number generator to control breeding.
    private static final Random rand = new Random();

    public Rabbit(boolean randomAge){
        initialize(randomAge);
    }

    public int getBreedingAge() {
        return breedingAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public double getBreedingProbability() {
        return breedingProbability;
    }

    public int getMaxLitterSize() {
        return maxLitterSize;
    }

    @Override
    public void initialize(boolean randomAge) {
        super.initialize(randomAge);
        setAge(0);
        if (randomAge) {
            setAge(rand.nextInt(maxAge));
        }
    }

    /**
     * This is what the rabbit does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     *
     * @param updatedField The field to transfer to.
     * @param newActors   A list to add newly born rabbits to.
     */
    @Override
    public void act(Field currentField, Field updatedField, List<Actor> newActors) {
        incrementAge();
        if (isAlive()) {
            int births = breed();
            for (int b = 0; b < births; b++) {
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                Rabbit newRabbit = (Rabbit) this.clone();
                newRabbit.setLocation(loc);
                newActors.add(newRabbit);
                updatedField.place(newRabbit, loc);
            }
            Location newLocation = updatedField.freeAdjacentLocation(getLocation());
            // Only transfer to the updated field if there was a free location
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                // can neither move nor stay - overcrowding - all locations taken
                setAlive(false);
            }
        }
    }

    /**
     * Increase the age.
     * This could result in the rabbit's death.
     */
    @Override
    protected void incrementAge() {
        setAge(getAge()+1);
        if (getAge() > maxAge) {
            setAlive(false);
        }
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     *
     * @return The number of births (may be zero).
     */
    @Override
    protected int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= breedingProbability) {
            births = rand.nextInt(maxLitterSize) + 1;
        }
        return births;
    }

    /**
     * A rabbit can breed if it has reached the breeding age.
     *
     * @return true if the rabbit can breed, false otherwise.
     */
    @Override
    protected boolean canBreed() {
        return getAge() >= breedingAge;
    }

    @Override
    public Animal clone() {
        return new RabbitBuilder(this.isRandomAge())
                .breedingAge(this.getBreedingAge())
                .maxAge(this.getMaxAge())
                .breedingProbability(this.getBreedingProbability())
                .maxLitterSize(this.getMaxLitterSize())
                .build();
    }

    public static class RabbitBuilder {
        private Integer breedingAge;
        private Integer maxAge;
        private Double breedingProbability;
        private Integer maxLitterSize;
        private boolean randomAge;

        public RabbitBuilder(boolean randomAge){
            this.randomAge = randomAge;
        }

        public RabbitBuilder breedingAge(int value){
            this.breedingAge = value;
            return this;
        }

        public RabbitBuilder maxAge(int value){
            this.maxAge = value;
            return this;
        }

        public RabbitBuilder breedingProbability(double value){
            this.breedingProbability = value;
            return this;
        }

        public RabbitBuilder maxLitterSize(int value){
            this.maxLitterSize = value;
            return this;
        }

        public RabbitBuilder randomAge(boolean value){
            this.randomAge = value;
            return this;
        }

        public Rabbit build(){
            Rabbit rabbit = new Rabbit(randomAge);
            if(this.breedingAge != null)
                rabbit.breedingAge = this.breedingAge;
            if(this.maxAge != null)
                rabbit.maxAge = this.maxAge;
            if(this.breedingProbability != null)
                rabbit.breedingProbability = this.breedingProbability;
            if(this.maxLitterSize != null)
                rabbit.maxLitterSize = this.maxLitterSize;
            return rabbit;
        }


    }

}
