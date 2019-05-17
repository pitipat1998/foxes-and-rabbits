package io.muzoo.ooc.ecosystems.actor.animal;

import io.muzoo.ooc.ecosystems.actor.Actor;
import io.muzoo.ooc.ecosystems.Field;
import io.muzoo.ooc.ecosystems.Location;

import java.util.List;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2002.10.28
 */
public class Rabbit extends Animal {

    private Rabbit(boolean randomAge){
        super(randomAge);

    }

    @Override
    protected void initialize(){
        setAge(0);
        if (isRandomAge()) {
            setAge(rand.nextInt(getMaxAge()));
        }
    }

    /**
     * This is what the rabbit does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     *
     * @param updatedField The field to transfer to.
     * @param newActors   A list to add newly born rabbits to.
     */
    @Override
    public void act(Field currentField, Field updatedField, List<Actor> newActors) {
        incrementAge();
        if (isAlive()) {
            int births = breed();
            for (int b = 0; b < births; b++) {
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                Rabbit newRabbit = (Rabbit) this.clone();
                newRabbit.setLocation(loc);
                newActors.add(newRabbit);
                updatedField.place(newRabbit, loc);
            }
            Location newLocation = updatedField.freeAdjacentLocation(getLocation());
            // Only transfer to the updated field if there was a free location
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
    public Animal clone() {
        return new RabbitBuilder(this.isRandomAge())
                .breedingAge(this.getBreedingAge())
                .maxAge(this.getMaxAge())
                .breedingProbability(this.getBreedingProbability())
                .maxLitterSize(this.getMaxLitterSize())
                .build();
    }

    public static class RabbitBuilder extends AnimalBuilder<RabbitBuilder>{

        public RabbitBuilder(boolean randomAge){
            super(randomAge);
            breedingAge(5);
            maxAge(50);
            breedingProbability(0.15);
            maxLitterSize(5);
        }

        @Override
        protected RabbitBuilder self(){
            return this;
        }

        public Rabbit build(){
            Rabbit rabbit = new Rabbit(this.isRandomAge());
            rabbit.setBreedingAge(this.getBreedingAge());
            rabbit.setMaxAge(this.getMaxAge());
            rabbit.setBreedingProbability(this.getBreedingProbability());
            rabbit.setMaxLitterSize(this.getMaxLitterSize());
            return rabbit;
        }


    }

}
