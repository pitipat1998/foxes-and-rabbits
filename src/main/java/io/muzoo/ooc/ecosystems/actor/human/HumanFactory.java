package io.muzoo.ooc.ecosystems.actor.human;

import io.muzoo.ooc.ecosystems.actor.Actor;
import io.muzoo.ooc.ecosystems.actor.ActorFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class HumanFactory implements ActorFactory {

    // singleton object
    private static HumanFactory humanFactory = null;
    // Randomizer
    private Random rand = new Random();

    // The probability that a fox will be created in any given grid position.
    private static final double HUNTER_CREATION_PROBABILITY = 0.02;

    private Hunter hunter = new Hunter.HunterBuilder().build();

    // Map of all animals
    private Map<Human, Double> humanProbability
            = new LinkedHashMap<Human, Double>() {
        {
            put(hunter, HUNTER_CREATION_PROBABILITY);
        }
    };
    private HumanFactory() {}

    public static HumanFactory getInstance(){
        if (humanFactory == null){
            return new HumanFactory();
        }
        return humanFactory;
    }

    @Override
    public Actor create(int row, int col){
        double r = rand.nextDouble();
        for (Map.Entry<Human, Double> entry :
                humanProbability.entrySet()) {
            Human type = entry.getKey();
            Double prob = entry.getValue();
            Human human = null;
            if (r < prob) {
                human = type.clone();
                human.setLocation(row, col);
                return human;
            }
            else
                r -= prob;
        }
        return null;
    }



}
