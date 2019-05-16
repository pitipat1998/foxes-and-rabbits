package io.muzoo.ooc.ecosystems;

import io.muzoo.ooc.ecosystems.actor.AbstractActorFactory;
import io.muzoo.ooc.ecosystems.actor.Actor;
import io.muzoo.ooc.ecosystems.actor.ActorFactory;
import io.muzoo.ooc.ecosystems.actor.animal.AnimalFactory;

import java.util.Collections;
import java.util.List;

public class PopulationGenerator {


    // Singleton instance of PopulationGenerator
    private static PopulationGenerator populationGenerator = null;

    private PopulationGenerator(){ }

    public static PopulationGenerator getInstance(){
        if(null == populationGenerator) {
            populationGenerator = new PopulationGenerator();
        }
        return populationGenerator;
    }

    /**
     * Populate a field with foxes and rabbits.
     *
     * @param field The field to be populated.
     */
    public void populate(Field field, List<Actor> actors) {
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                ActorFactory actorFactory = AbstractActorFactory.createRandom();
                if(actorFactory != null) {
                    Actor actor = actorFactory.createRandom(row, col);
                    if (actor != null) {
                        actors.add(actor);
                        field.place(actor, row, col);
                    }
                }
                // else leave the location empty.
            }
        }
        Collections.shuffle(actors);
    }
}
