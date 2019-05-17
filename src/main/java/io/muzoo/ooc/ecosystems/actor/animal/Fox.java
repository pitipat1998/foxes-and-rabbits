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

    public static class FoxBuilder extends AnimalBuilder<FoxBuilder> {

        public FoxBuilder(boolean randomAge){
            super(randomAge);
            breedingAge(30);
            maxAge(150);
            breedingProbability(0.2);
            maxLitterSize(3);
            addFoods(Rabbit.class, 4);
        }


        @Override
        protected FoxBuilder self(){
            return this;
        }

        public Fox build(){
            Fox fox = new Fox();
            fox.setRandomAge(this.isRandomAge());
            fox.setBreedingAge(this.getBreedingAge());
            fox.setMaxAge(this.getMaxAge());
            fox.setBreedingProbability(this.getBreedingProbability());
            fox.setMaxLitterSize(this.getMaxLitterSize());
            fox.setFoods(this.getFoods());
            fox.initialize();
            return fox;
        }


    }

}
