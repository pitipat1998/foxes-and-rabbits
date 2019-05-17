package io.muzoo.ooc.ecosystems.actor.animal;

import io.muzoo.ooc.ecosystems.actor.ActorFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class AnimalFactory implements ActorFactory {

    // singleton object
    private static AnimalFactory animalFactory = null;

    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.05;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.10;
    // The probability that a tiger will be created in any given grid position
    private static final double TIGER_CREATION_PROBABILITY = 0.03;

    private AnimalFactory() {}

    public static AnimalFactory getInstance(){
        if (animalFactory == null){
            return new AnimalFactory();
        }
        return animalFactory;
    }


    private Rabbit rabbit = new Rabbit.RabbitBuilder(true).build();
    private Fox fox = new Fox.FoxBuilder(true).build();
    private Tiger tiger = new Tiger.TigerBuilder(true).build();

    // Randomizer
    private Random rand = new Random();
    // Map of all animals
    private Map<Animal, Double> animalProbabilities
            = new LinkedHashMap<Animal, Double>() {
        {
            put(rabbit, RABBIT_CREATION_PROBABILITY);
            put(fox, FOX_CREATION_PROBABILITY);
            put(tiger, TIGER_CREATION_PROBABILITY);
        }
    };

    public Animal create(int row, int col){
        double r = rand.nextDouble();
        for (Map.Entry<Animal, Double> entry :
                animalProbabilities.entrySet()) {
            Animal type = entry.getKey();
            Double prob = entry.getValue();
            Animal animal = null;
            if (r < prob){
                animal = type.clone();
                animal.setLocation(row, col);
                return animal;
            }
            else
                r -= prob;
        }
        return null;
    }
}
