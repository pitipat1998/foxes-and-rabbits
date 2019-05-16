package io.muzoo.ooc.ecosystems.actor;

import io.muzoo.ooc.ecosystems.actor.animal.AnimalFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class AbstractActorFactory {
    private static final double ANIMAL_CREATION_PROBABILITY = 1;
    private static final double HUMAN_CREATION_PROBABILITY = 0.5;

    private static Random rand = new Random();
    private static Map<Class<? extends ActorFactory>, Double> actorFactoryProbabilities
            = new LinkedHashMap<Class<? extends ActorFactory>, Double>() {
        {
            put(AnimalFactory.class, ANIMAL_CREATION_PROBABILITY);
        }
    };

    public static ActorFactory createRandom(){
        double r = rand.nextDouble();
        for (Map.Entry<Class<? extends ActorFactory>, Double> entry :
                actorFactoryProbabilities.entrySet()) {
            Class<? extends ActorFactory> type = entry.getKey();
            Double prob = entry.getValue();
            if (r < prob) {
                ActorFactory actorFactory = null;
                try {
                    Method method = type.getMethod("getInstance", null);
                    actorFactory = (ActorFactory) method.invoke(null, null);
                } catch (NoSuchMethodException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e2) {
                    e2.printStackTrace();
                } catch (IllegalAccessException e3) {
                    e3.printStackTrace();
                }
                return actorFactory;
            }
            else
                r -= prob;
        }
        return null;
    }
}
