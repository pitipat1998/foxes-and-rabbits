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

    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private int rabbitFoodValue = 4;

    // Individual characteristics (instance fields).

    // The fox's food level, which is increased by eating rabbits.
    private int foodLevel;

    private Fox(boolean randomAge){
        super(randomAge);

    }

    @Override
    protected void initialize(){
        setAge(0);
        if (isRandomAge()) {
            setAge(rand.nextInt(getMaxAge()));
            foodLevel = rand.nextInt(rabbitFoodValue);
        } else {
            // leave age at 0
            foodLevel = rabbitFoodValue;
        }
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
    public Animal clone() {
        return new FoxBuilder(this.isRandomAge())
                .breedingAge(this.getBreedingAge())
                .maxAge(this.getMaxAge())
                .breedingProbability(this.getBreedingProbability())
                .maxLitterSize(this.getMaxLitterSize())
                .rabbitFoodValue(this.getRabbitFoodValue())
                .build();
    }

    public static class FoxBuilder extends AnimalBuilder<FoxBuilder> {
        private Integer rabbitFoodValue;

        public FoxBuilder(boolean randomAge){
            super(randomAge);
            breedingAge(30);
            maxAge(150);
            breedingProbability(0.2);
            maxLitterSize(3);
            rabbitFoodValue(4);
        }

        public FoxBuilder rabbitFoodValue(int value){
            this.rabbitFoodValue = value;
            return self();
        }

        @Override
        protected FoxBuilder self(){
            return this;
        }

        public Fox build(){
            Fox fox = new Fox(this.isRandomAge());
            fox.setBreedingAge(this.getBreedingAge());
            fox.setMaxAge(this.getMaxAge());
            fox.setBreedingProbability(this.getBreedingProbability());
            fox.setMaxLitterSize(this.getMaxLitterSize());
            fox.rabbitFoodValue = this.rabbitFoodValue;
            fox.initialize();
            return fox;
        }


    }

}
