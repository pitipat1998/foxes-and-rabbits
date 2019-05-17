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

    public static class TigerBuilder extends AnimalBuilder<TigerBuilder>{

        public TigerBuilder(boolean randomAge){
            super(randomAge);
            breedingAge(50);
            maxAge(150);
            breedingProbability(0.15);
            maxLitterSize(2);
            addFoods(Rabbit.class, 4);
            addFoods(Fox.class, 4);
        }

        @Override
        protected TigerBuilder self(){
            return this;
        }

        public Tiger build(){
            Tiger tiger = new Tiger();
            tiger.setRandomAge(this.isRandomAge());
            tiger.setBreedingAge(this.getBreedingAge());
            tiger.setMaxAge(this.getMaxAge());
            tiger.setBreedingProbability(this.getBreedingProbability());
            tiger.setMaxLitterSize(this.getMaxLitterSize());
            tiger.setFoods(this.getFoods());
            tiger.initialize();
            return tiger;
        }

    }
}
