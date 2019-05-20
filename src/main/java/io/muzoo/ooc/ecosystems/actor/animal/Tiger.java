package io.muzoo.ooc.ecosystems.actor.animal;

public class Tiger extends Animal<Tiger> {

    private Tiger() { super(); }

    @Override
    public Tiger clone() {
        return new TigerBuilder(this.isRandomAge())
                .breedingAge(this.getBreedingAge())
                .maxAge(this.getMaxAge())
                .breedingProbability(this.getBreedingProbability())
                .maxLitterSize(this.getMaxLitterSize())
                .foods(this.getFoods())
                .build();
    }

    public static class TigerBuilder extends AnimalBuilder<TigerBuilder, Tiger>{

        public TigerBuilder(boolean randomAge){
            super(randomAge);
        }

        @Override
        protected TigerBuilder self(){
            return this;
        }

        public Tiger build(){
            Tiger tiger = new Tiger();
            buildAnimal(tiger);
            return tiger;
        }

    }
}
