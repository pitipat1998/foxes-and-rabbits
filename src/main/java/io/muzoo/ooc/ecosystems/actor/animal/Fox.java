package io.muzoo.ooc.ecosystems.actor.animal;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2002.10.28
 */
public class Fox extends Animal<Fox> {

    private Fox(){ super(); }

    @Override
    public Fox clone() {
        return new FoxBuilder(this.isRandomAge())
                .breedingAge(this.getBreedingAge())
                .maxAge(this.getMaxAge())
                .breedingProbability(this.getBreedingProbability())
                .maxLitterSize(this.getMaxLitterSize())
                .foods(this.getFoods())
                .build();
    }

    public static class FoxBuilder extends AnimalBuilder<FoxBuilder, Fox> {

        public FoxBuilder(boolean randomAge){
            super(randomAge);
        }

        @Override
        protected FoxBuilder self(){
            return this;
        }

        public Fox build(){
            Fox fox = new Fox();
            buildAnimal(fox);
            return fox;
        }


    }

}
