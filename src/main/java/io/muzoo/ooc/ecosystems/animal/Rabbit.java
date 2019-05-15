package io.muzoo.ooc.ecosystems.animal;

import io.muzoo.ooc.ecosystems.Actor;
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
    private static final int BREEDING_AGE = 5;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 50;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.15;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;
    // A shared random number generator to control breeding.
    private static final Random rand = new Random();

    @Override
    public void initialize(boolean randomAge) {
        super.initialize(randomAge);
        setAge(0);
        if (randomAge) {
            setAge(rand.nextInt(MAX_AGE));
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
                Rabbit newRabbit = (Rabbit) AnimalFactory.createAnimal(Rabbit.class.getSimpleName(), false, loc);
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
        if (getAge() > MAX_AGE) {
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
        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
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
        return getAge() >= BREEDING_AGE;
    }

    /**
     * Tell the rabbit that it's dead now :(
     */
    public void setEaten() {
        setAlive(false);
    }

}
