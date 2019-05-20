package io.muzoo.ooc.ecosystems.actor.animal;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2002.10.28
 */
public class Rabbit extends Animal<Rabbit> {

    private Rabbit(){ super(); }

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

    public static class RabbitBuilder extends AnimalBuilder<RabbitBuilder, Rabbit>{

        public RabbitBuilder(boolean randomAge){
            super(randomAge);
        }

        @Override
        protected RabbitBuilder self(){
            return this;
        }

        public Rabbit build(){
            Rabbit rabbit = new Rabbit();
            buildAnimal(rabbit);
            return rabbit;
        }
    }

}
