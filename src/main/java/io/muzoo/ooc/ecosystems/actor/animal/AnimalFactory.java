package io.muzoo.ooc.ecosystems.actor.animal;

import io.muzoo.ooc.ecosystems.Location;
import io.muzoo.ooc.ecosystems.actor.Actor;
import io.muzoo.ooc.ecosystems.actor.ActorFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class AnimalFactory implements ActorFactory {

    // singleton object
    private static AnimalFactory animalFactory = null;

    // The probability that a fox will be created in any given grid position.
    private final double FOX_CREATION_PROBABILITY = 0.04;
    // The probability that a rabbit will be created in any given grid position.
    private final double RABBIT_CREATION_PROBABILITY = 0.10;
    // The probability that a tiger will be created in any given grid position
    private final double TIGER_CREATION_PROBABILITY = 0.01;

    // Randomizer
    private Random rand = new Random();
    // Map of all animals
    private Map<Class<? extends Animal>, Double> animalProbabilities
            = new LinkedHashMap<Class<? extends Animal>, Double>() {
        {
            put(Rabbit.class, RABBIT_CREATION_PROBABILITY);
            put(Fox.class, FOX_CREATION_PROBABILITY);
            put(Tiger.class, TIGER_CREATION_PROBABILITY);
        }
    };

    private Map<String, Class<? extends Animal>> registeredAnimals
            = new HashMap<String, Class<? extends Animal>>() {
        {
            put(Rabbit.class.getSimpleName(), Rabbit.class);
            put(Fox.class.getSimpleName(), Fox.class);
            put(Tiger.class.getSimpleName(), Tiger.class);
        }
    };

    public static AnimalFactory getInstance(){
        if (animalFactory == null){
            return new AnimalFactory();
        }
        return animalFactory;
    }

    @Override
    public Actor createRandom(int row, int col) {
        return createRandom(true, row, col);
    }

    public Animal createRandom(boolean randomAge, int row, int col){
        double r = rand.nextDouble();
        for (Map.Entry<Class<? extends Animal>, Double> entry :
                animalProbabilities.entrySet()) {
            Class<? extends Animal> type = entry.getKey();
            Double prob = entry.getValue();
            if (r < prob) {
                Animal animal = null;
                try {
                    animal = type.newInstance();
                    animal.initialize(randomAge);
                    animal.setLocation(row, col);
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e2) {
                    e2.printStackTrace();
                }
                return animal;
            }
            else
                r -= prob;
        }
        return null;
    }

    @Override
    public Actor create(String type, Location loc) {
        return create(type, false, loc);
    }

    public Animal create(String animalType, boolean randomAge, Location loc){
        Class<? extends Animal> type = registeredAnimals.get(animalType);
        Animal animal = null;
        try {
            animal = type.newInstance();
            animal.initialize(randomAge);
            animal.setLocation(loc);
        } catch (InstantiationException | IllegalAccessException ex) {
        } catch (NullPointerException npe) {
            System.out.println("Not found animalType: " + animalType);
        }
        return animal;
    }
}
