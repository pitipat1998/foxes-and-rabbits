package io.muzoo.ooc.ecosystems.actor.animal;

import io.muzoo.ooc.ecosystems.Field;
import io.muzoo.ooc.ecosystems.Location;
import io.muzoo.ooc.ecosystems.actor.Actor;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Tiger extends Animal implements Predator{
    // The age at which a tiger can start to breed.
    private int breedingAge = 50;
    // The age to which a tiger can live.
    private int maxAge = 150;
    // The likelihood of a tiger breeding.
    private double breedingProbability = 0.1;
    // The maximum number of births.
    private int maxLitterSize = 2;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a tiger can go before it has to eat again.
    private int rabbitFoodValue = 4;
    // The food value of a single fox. In effect, this is the
    // number of steps a tiger can go before it has to eat again.
    private int foxFoodValue = 5;
    // A shared random number generator to control breeding.
    private static final Random rand = new Random();

    private int[] foods = {foxFoodValue, rabbitFoodValue};
    // The fox's food level, which is increased by eating rabbits.
    private int foodLevel;

    public Tiger(boolean randomAge){
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

    public int getRabbitFoodValue() {
        return rabbitFoodValue;
    }

    public int getFoxFoodValue() {
        return foxFoodValue;
    }

    @Override
    public void initialize(boolean randomAge) {
        super.initialize(randomAge);
        int foodIdx = rand.nextInt(foods.length);
        if (randomAge) {
            setAge(rand.nextInt(maxAge));
            foodLevel = rand.nextInt(foods[foodIdx]);
        } else {
            // leave age at 0
            foodLevel = foods[foodIdx];
        }
    }

    @Override
    protected void incrementAge() {
        setAge(getAge()+1);
        if (getAge() > maxAge) {
            setAlive(false);
        }
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
    public void act(Field currentField, Field updatedField, List<Actor> newActors) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            // New foxes are born into adjacent locations.
            int births = breed();
            for (int b = 0; b < births; b++) {
                Location loc = updatedField.randomAdjacentLocation(getLocation());
//                Tiger newTiger = (Tiger) animalFactory.create(Tiger.class.getSimpleName(), false, loc);
                Tiger newTiger = new Tiger(false);
                newTiger.setLocation(loc);
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
                    foodLevel = rabbitFoodValue;
                    return where;
                }
            }
            else if (actor instanceof Fox) {
                Fox fox = (Fox) actor;
                if (fox.isAlive()) {
                    fox.setDead();
                    foodLevel = foxFoodValue;
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

    @Override
    public Animal clone() {
        return new TigerBuilder(this.isRandomAge())
                .breedingAge(this.getBreedingAge())
                .maxAge(this.getMaxAge())
                .breedingProbability(this.getBreedingProbability())
                .maxLitterSize(this.getMaxLitterSize())
                .rabbitFoodValue(this.getRabbitFoodValue())
                .foxFoodValue(this.getFoxFoodValue())
                .build();
    }

    public static class TigerBuilder {
        private Integer breedingAge;
        private Integer maxAge;
        private Double breedingProbability;
        private Integer maxLitterSize;
        private Integer rabbitFoodValue;
        private Integer foxFoodValue;
        private boolean randomAge;

        public TigerBuilder(boolean randomAge){
            this.randomAge = randomAge;
        }

        public TigerBuilder breedingAge(int value){
            this.breedingAge = value;
            return this;
        }

        public TigerBuilder maxAge(int value){
            this.maxAge = value;
            return this;
        }

        public TigerBuilder breedingProbability(double value){
            this.breedingProbability = value;
            return this;
        }

        public TigerBuilder maxLitterSize(int value){
            this.maxLitterSize = value;
            return this;
        }

        public TigerBuilder randomAge(boolean value){
            this.randomAge = value;
            return this;
        }

        public TigerBuilder rabbitFoodValue(int value){
            this.rabbitFoodValue = value;
            return this;
        }

        public TigerBuilder foxFoodValue(int value){
            this.foxFoodValue = value;
            return this;
        }

        public Tiger build(){
            Tiger tiger = new Tiger(randomAge);
            if(this.breedingAge != null)
                tiger.breedingAge = this.breedingAge;
            if(this.maxAge != null)
                tiger.maxAge = this.maxAge;
            if(this.breedingProbability != null)
                tiger.breedingProbability = this.breedingProbability;
            if(this.maxLitterSize != null)
                tiger.maxLitterSize = this.maxLitterSize;
            if(this.rabbitFoodValue != null)
                tiger.rabbitFoodValue = this.rabbitFoodValue;
            if(this.foxFoodValue != null)
                tiger.foxFoodValue = this.foxFoodValue;
            return tiger;
        }

    }
}
