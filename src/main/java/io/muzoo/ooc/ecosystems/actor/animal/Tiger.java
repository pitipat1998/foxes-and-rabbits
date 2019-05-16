package io.muzoo.ooc.ecosystems.actor.animal;

import io.muzoo.ooc.ecosystems.Field;
import io.muzoo.ooc.ecosystems.Location;
import io.muzoo.ooc.ecosystems.actor.Actor;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Tiger extends Animal implements Predator{
    // The age at which a tiger can start to breed.
    private static final int BREEDING_AGE = 50;
    // The age to which a tiger can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a tiger breeding.
    private static final double BREEDING_PROBABILITY = 0.05;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a tiger can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 5;
    // The food value of a single fox. In effect, this is the
    // number of steps a tiger can go before it has to eat again.
    private static final int FOX_FOOD_VALUE = 6;
    // A shared random number generator to control breeding.
    private static final Random rand = new Random();

    private int[] foods = {FOX_FOOD_VALUE, RABBIT_FOOD_VALUE};
    // The fox's food level, which is increased by eating rabbits.
    private int foodLevel;


    @Override
    public void initialize(boolean randomAge) {
        super.initialize(randomAge);
        int foodIdx = rand.nextInt(foods.length);
        if (randomAge) {
            setAge(rand.nextInt(MAX_AGE));
            foodLevel = rand.nextInt(foods[foodIdx]);
        } else {
            // leave age at 0
            foodLevel = foods[foodIdx];
        }
    }

    @Override
    protected void incrementAge() {
        setAge(getAge()+1);
        if (getAge() > MAX_AGE) {
            setAlive(false);
        }
    }

    @Override
    protected boolean canBreed() {
        return getAge() >= BREEDING_AGE;
    }

    @Override
    protected int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    @Override
    public void act(Field currentField, Field updatedField, List<Actor> newActors) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            // New foxes are born into adjacent locations.
            int births = breed();
            for (int b = 0; b < births; b++) {
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                Tiger newTiger = (Tiger) animalFactory.create(Tiger.class.getSimpleName(), false, loc);
                newActors.add(newTiger);
                updatedField.place(newTiger, loc);
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

    @Override
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
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
            else if (actor instanceof Fox) {
                Fox fox = (Fox) actor;
                if (fox.isAlive()) {
                    fox.setDead();
                    foodLevel = FOX_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }

    @Override
    public void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setAlive(false);
        }
    }
}
