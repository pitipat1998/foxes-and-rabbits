package io.muzoo.ooc.ecosystems;

import io.muzoo.ooc.ecosystems.animal.Animal;
import io.muzoo.ooc.ecosystems.animal.AnimalFactory;
import io.muzoo.ooc.ecosystems.animal.Fox;
import io.muzoo.ooc.ecosystems.animal.Rabbit;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PopulationGenerator {


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
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Animal animal = AnimalFactory.createRandomAnimal(true, row, col);
                if (animal != null){
                    animals.add(animal);
                    field.place(animal, row, col);
                }
                // else leave the location empty.
            }
        }
        Collections.shuffle(animals);
    }
}
