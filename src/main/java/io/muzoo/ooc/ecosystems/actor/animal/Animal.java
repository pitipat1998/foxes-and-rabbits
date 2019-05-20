package io.muzoo.ooc.ecosystems.actor.animal;

import io.muzoo.ooc.ecosystems.Field;
import io.muzoo.ooc.ecosystems.actor.Actor;
import io.muzoo.ooc.ecosystems.Location;

import java.util.*;

public abstract class Animal<T extends Animal<T>> implements Actor {

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
    // animal food level
    private int foodLevel;
    // animal foods
    private Map<String, Integer> foods;
    // The animal's position
    private Location location;

    private boolean randomAge;
    // A shared random number generator to control breeding.
    private static final Random rand = new Random();

    protected Animal(){
        this.alive = true;
        foods = new HashMap<>();
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

    public int getFoodLevel() { return foodLevel; }

    public void setFoodLevel(int foodLevel) { this.foodLevel = foodLevel; }

    public Map<String, Integer> getFoods() { return foods; }

    public void setFoods(Map<String, Integer> foods) {
        this.foods = foods;
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

    public int getAge() { return age; }

    public void setAge(int age) { this.age = age; }

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
     * get location of the animal
     *
     * @return location of the animal
     */
    public Location getLocation() {
        return location;
    }

    protected void initialize(){

        if (isRandomAge())
            setAge(rand.nextInt(getMaxAge()));
        else
            setAge(0);

        if (foods.size() > 0) {
            List<Integer> foodlist = new ArrayList<>(getFoods().values());
            int foodIdx = rand.nextInt(foodlist.size());
            if (isRandomAge()) {
                setFoodLevel(rand.nextInt(foodlist.get(foodIdx)));
            } else {
                setFoodLevel(foodlist.get(foodIdx));
            }
        }
    }

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

    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    protected void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setAlive(false);
        }
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
                Animal newAnimal = this.clone();
                newAnimal.setLocation(loc);
                newActors.add(newAnimal);
                updatedField.place(newAnimal, loc);
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
     * Tell the animal to look for rabbits adjacent to its current location.
     *
     * @param field    The field in which it must look.
     * @param location Where in the field it is located.
     * @return Where food was found, or null if it wasn't.
     */
    public Location findFood(Field field, Location location) {
        Iterator adjacentLocations =
                field.adjacentLocations(location);
        Set<Map.Entry<String, Integer>> animalFoods = foods.entrySet();
        while (adjacentLocations.hasNext()) {
            Location where = (Location) adjacentLocations.next();
            Actor actor = field.getObjectAt(where);
            if(actor != null){
                for(Map.Entry<String, Integer> food : animalFoods){
                    if (actor.getClass().getSimpleName().equals(food.getKey())) {
                        if (actor.isActive()) {
                            actor.setActive(false);
                            foodLevel = food.getValue();
                            return where;
                        }
                    }
                }
            }
        }
        return null;
    }

    public abstract T clone();


    public abstract static class AnimalBuilder<A extends AnimalBuilder<A, B>, B extends Animal<B>> {
        private Integer breedingAge;
        private Integer maxAge;
        private Double breedingProbability;
        private Integer maxLitterSize;
        private Map<String, Integer> foods;
        private boolean randomAge;

        public Integer getBreedingAge() { return breedingAge; }

        public Integer getMaxAge() { return maxAge; }

        public Double getBreedingProbability() { return breedingProbability; }

        public Integer getMaxLitterSize() { return maxLitterSize; }

        public boolean isRandomAge() { return randomAge; }

        public Map<String, Integer> getFoods() { return foods; }

        public AnimalBuilder(boolean randomAge){
            this.randomAge = randomAge;
            foods = new HashMap<>();
        }

        public A breedingAge(int value){
            this.breedingAge = value;
            return self();
        }

        public A maxAge(int value){
            this.maxAge = value;
            return self();
        }

        public A breedingProbability(double value){
            this.breedingProbability = value;
            return self();
        }

        public A maxLitterSize(int value){
            this.maxLitterSize = value;
            return self();
        }

        public A foods(Map<String, Integer> map){
            this.foods = map;
            return self();
        }

        public A addFoods(Class<? extends io.muzoo.ooc.ecosystems.actor.animal.Animal> animal, int foodValue){
            this.foods.put(animal.getSimpleName(), foodValue);
            return self();
        }

        protected abstract A self();

        protected void buildAnimal(Animal animal){
            animal.setRandomAge(this.isRandomAge());
            animal.setBreedingAge(this.getBreedingAge());
            animal.setMaxAge(this.getMaxAge());
            animal.setBreedingProbability(this.getBreedingProbability());
            animal.setMaxLitterSize(this.getMaxLitterSize());
            animal.setFoods(this.getFoods());
            animal.initialize();
        }

        public abstract B build();


    }

}
