package io.muzoo.ooc.ecosystems.actor.animal;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2002.10.28
 */
public class Rabbit extends Animal<Rabbit> {

    @Override
    public void incrementHunger() { }

    @Override
    public Rabbit clone() {
        return new RabbitBuilder(this.isRandomAge())
                .breedingAge(this.getBreedingAge())
                .maxAge(this.getMaxAge())
                .breedingProbability(this.getBreedingProbability())
                .maxLitterSize(this.getMaxLitterSize())
                .foods(this.getFoods())
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
            Rabbit rabbit = new Rabbit();
            rabbit.setRandomAge(this.isRandomAge());
            rabbit.setBreedingAge(this.getBreedingAge());
            rabbit.setMaxAge(this.getMaxAge());
            rabbit.setBreedingProbability(this.getBreedingProbability());
            rabbit.setMaxLitterSize(this.getMaxLitterSize());
            rabbit.setFoods(this.getFoods());
            return rabbit;
        }


    }

}
