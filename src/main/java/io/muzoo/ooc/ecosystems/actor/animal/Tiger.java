package io.muzoo.ooc.ecosystems.actor.animal;

import io.muzoo.ooc.ecosystems.Field;
import io.muzoo.ooc.ecosystems.Location;
import io.muzoo.ooc.ecosystems.actor.Actor;

import java.util.Iterator;
import java.util.List;

public class Tiger extends Animal implements Predator{

    // The food value of a single rabbit. In effect, this is the
    // number of steps a tiger can go before it has to eat again.
    private int rabbitFoodValue;
    // The food value of a single fox. In effect, this is the
    // number of steps a tiger can go before it has to eat again.
    private int foxFoodValue;

    // The fox's food level, which is increased by eating rabbits.
    private int foodLevel;

    private Tiger(boolean randomAge){
        super(randomAge);

    }

    protected void initialize(){
        int[] foods = {foxFoodValue, rabbitFoodValue};
        int foodIdx = rand.nextInt(foods.length);
        if (isRandomAge()) {
            setAge(rand.nextInt(getMaxAge()));
            foodLevel = rand.nextInt(foods[foodIdx]);
        } else {
            // leave age at 0
            foodLevel = foods[foodIdx];
        }
    }

    public int getRabbitFoodValue() {
        return rabbitFoodValue;
    }

    public int getFoxFoodValue() {
        return foxFoodValue;
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

    public static class TigerBuilder extends AnimalBuilder<TigerBuilder>{
        private Integer rabbitFoodValue;
        private Integer foxFoodValue;

        public TigerBuilder(boolean randomAge){
            super(randomAge);
            breedingAge(50);
            maxAge(150);
            breedingProbability(0.4);
            maxLitterSize(2);
            rabbitFoodValue = 4;
            foxFoodValue = 6;
        }

        public TigerBuilder rabbitFoodValue(int value){
            this.rabbitFoodValue = value;
            return self();
        }

        public TigerBuilder foxFoodValue(int value){
            this.foxFoodValue = value;
            return self();
        }

        @Override
        protected TigerBuilder self(){
            return this;
        }

        public Tiger build(){
            Tiger tiger = new Tiger(isRandomAge());
            tiger.setBreedingAge(this.getBreedingAge());
            tiger.setMaxAge(this.getMaxAge());
            tiger.setBreedingProbability(this.getBreedingProbability());
            tiger.setMaxLitterSize(this.getMaxLitterSize());
            tiger.rabbitFoodValue = this.rabbitFoodValue;
            tiger.foxFoodValue = this.foxFoodValue;
            tiger.initialize();
            return tiger;
        }

    }
}
