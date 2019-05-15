package io.muzoo.ooc.ecosystems;

import io.muzoo.ooc.ecosystems.animal.Animal;
import io.muzoo.ooc.ecosystems.animal.Fox;
import io.muzoo.ooc.ecosystems.animal.Rabbit;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PopulationGenerator {

    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;

    // Singleton instance of PopulationGenerator
    private static PopulationGenerator populationGenerator = null;


    private PopulationGenerator(){

    }

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
    public void populate(Field field, List<Animal> animals) {
        Random rand = new Random();
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                if (rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Fox fox = new Fox(true);
                    animals.add(fox);
                    fox.setLocation(row, col);
                    field.place(fox, row, col);
                } else if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Rabbit rabbit = new Rabbit(true);
                    animals.add(rabbit);
                    rabbit.setLocation(row, col);
                    field.place(rabbit, row, col);
                }
                // else leave the location empty.
            }
        }
        Collections.shuffle(animals);
    }
}
