package io.muzoo.ooc.ecosystems.actor.human;

import io.muzoo.ooc.ecosystems.Location;
import io.muzoo.ooc.ecosystems.actor.Actor;
import io.muzoo.ooc.ecosystems.actor.ActorFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class HumanFactory implements ActorFactory {

    // singleton object
    private static HumanFactory humanFactory = null;

    private HumanFactory() {}

    // The probability that a fox will be created in any given grid position.
    private final double HUNTER_CREATION_PROBABILITY = 0.02;

    // Randomizer
    private Random rand = new Random();
    // Map of all animals
    private Map<Class<? extends Human>, Double> animalProbabilities
            = new LinkedHashMap<Class<? extends Human>, Double>() {
        {
            put(Hunter.class, HUNTER_CREATION_PROBABILITY);
        }
    };

    private Map<String, Class<? extends Human>> registeredAnimals
            = new HashMap<String, Class<? extends Human>>() {
        {
            put(Hunter.class.getSimpleName(), Hunter.class);
        }
    };

    public static HumanFactory getInstance(){
        if (humanFactory == null){
            return new HumanFactory();
        }
        return humanFactory;
    }


    @Override
    public Actor createRandom(int row, int col){
        double r = rand.nextDouble();
        for (Map.Entry<Class<? extends Human>, Double> entry :
                animalProbabilities.entrySet()) {
            Class<? extends Human> type = entry.getKey();
            Double prob = entry.getValue();
            if (r < prob) {
                Human human = null;
                try {
                    human = type.newInstance();
                    human.setLocation(row, col);
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e2) {
                    e2.printStackTrace();
                }
                return human;
            }
            else
                r -= prob;
        }
        return null;
    }

    @Override
    public Actor create(String humanType, Location loc){
        Class<? extends Human> type = registeredAnimals.get(humanType);
        Human human = null;
        try {
            human = type.newInstance();
            human.setLocation(loc);
        } catch (InstantiationException | IllegalAccessException ex) {
        } catch (NullPointerException npe) {
            System.out.println("Not found animalType: " + humanType);
        }
        return human;
    }

}
