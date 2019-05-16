package io.muzoo.ooc.ecosystems.actor.animal;

import io.muzoo.ooc.ecosystems.actor.Actor;
import io.muzoo.ooc.ecosystems.Field;
import io.muzoo.ooc.ecosystems.Location;

import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2002.10.28
 */
public class Fox extends Animal implements Predator {

    // Characteristics shared by all foxes (static fields).
    // The age at which a fox can start to breed.
    private int breedingAge = 30;
    // The age to which a fox can live.
    private int maxAge = 150;
    // The likelihood of a fox breeding.
    private double breedingProbability = 0.15;
    // The maximum number of births.
    private int maxLitterSize = 3;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private int rabbitFoodValue = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = new Random();

    // Individual characteristics (instance fields).

    // The fox's food level, which is increased by eating rabbits.
    private int foodLevel;

    private Fox(boolean randomAge){
        super(randomAge);
        setAge(0);
        if (randomAge) {
            setAge(rand.nextInt(maxAge));
            foodLevel = rand.nextInt(rabbitFoodValue);
        } else {
            // leave age at 0
            foodLevel = rabbitFoodValue;
        }
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

    public int getRabbitFoodValue() {
        return rabbitFoodValue;
    }

    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     *
     * @param currentField The field currently occupied.
     * @param updatedField The field to transfer to.
     * @param newActors     A list to add newly born foxes to.
     */

    @Override
    public void act(Field currentField, Field updatedField, List<Actor> newActors) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            // New foxes are born into adjacent locations.
            int births = breed();
            for (int b = 0; b < births; b++) {
                Location loc = updatedField.randomAdjacentLocation(getLocation());
//                Fox newFox = (Fox) animalFactory.create(Fox.class.getSimpleName(), false, loc);
                Fox newFox = (Fox) this.clone();
                newFox.setLocation(loc);
                newActors.add(newFox);
                updatedField.place(newFox, loc);
            }
            // Move towards the source of food if found.
            Location newLocation = findFood(currentField, getLocation());
            if (newLocation == null) {  // no food found - move randomly
                newLocation = updatedField.freeAdjacentLocation(getLocation());
            }
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
     * Increase the age. This could result in the fox's death.
     */
    protected void incrementAge() {
        setAge(getAge()+1);
        if (getAge() > maxAge) {
            setAlive(false);
        }
    }

    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    public void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setAlive(false);
        }
    }

    /**
     * Tell the fox to look for rabbits adjacent to its current location.
     *
     * @param field    The field in which it must look.
     * @param location Where in the field it is located.
     * @return Where food was found, or null if it wasn't.
     */
    public Location findFood(Field field, Location location) {
        Iterator adjacentLocations =
                field.adjacentLocations(location);
        while (adjacentLocations.hasNext()) {
            Location where = (Location) adjacentLocations.next();
            Actor actor = field.getObjectAt(where);
            if (actor instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) actor;
                if (rabbit.isAlive()) {
                    rabbit.setDead();
                    foodLevel = rabbitFoodValue;
                    return where;
                }
            }
        }
        return null;
    }

    @Override
    protected boolean canBreed() {
        return getAge() >= breedingAge;
    }

    @Override
    protected int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= breedingProbability) {
            births = rand.nextInt(maxLitterSize) + 1;
        }
        return births;
    }

    @Override
    public Animal clone() {
        return new FoxBuilder(this.isRandomAge())
                .breedingAge(this.getBreedingAge())
                .maxAge(this.getMaxAge())
                .breedingProbability(this.getBreedingProbability())
                .maxLitterSize(this.getMaxLitterSize())
                .rabbitFoodValue(this.getRabbitFoodValue())
                .build();
    }

    public static class FoxBuilder {
        private Integer breedingAge = 30;
        private Integer maxAge = 150;
        private Double breedingProbability = 0.2;
        private Integer maxLitterSize = 3;
        private Integer rabbitFoodValue = 4;
        private boolean randomAge;

        public FoxBuilder(boolean randomAge){
            this.randomAge = randomAge;
        }

        public FoxBuilder breedingAge(int value){
            this.breedingAge = value;
            return this;
        }

        public FoxBuilder maxAge(int value){
            this.maxAge = value;
            return this;
        }

        public FoxBuilder breedingProbability(double value){
            this.breedingProbability = value;
            return this;
        }

        public FoxBuilder maxLitterSize(int value){
            this.maxLitterSize = value;
            return this;
        }

        public FoxBuilder randomAge(boolean value){
            this.randomAge = value;
            return this;
        }

        public FoxBuilder rabbitFoodValue(int value){
            this.rabbitFoodValue = value;
            return this;
        }

        public Fox build(){
            Fox fox = new Fox(randomAge);
            if(this.breedingAge != null)
                fox.breedingAge = this.breedingAge;
            if(this.maxAge != null)
                fox.maxAge = this.maxAge;
            if(this.breedingProbability != null)
                fox.breedingProbability = this.breedingProbability;
            if(this.maxLitterSize != null)
                fox.maxLitterSize = this.maxLitterSize;
            if(this.rabbitFoodValue != null)
                fox.rabbitFoodValue = this.rabbitFoodValue;
            return fox;
        }


    }

}
