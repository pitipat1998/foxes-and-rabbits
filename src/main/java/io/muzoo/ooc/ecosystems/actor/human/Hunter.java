package io.muzoo.ooc.ecosystems.actor.human;

import io.muzoo.ooc.ecosystems.Field;
import io.muzoo.ooc.ecosystems.Location;
import io.muzoo.ooc.ecosystems.actor.Actor;

import java.util.List;

public class Hunter extends Human<Hunter> {

    // maximum number of shot
    private int maximumShots = 10;

    public int getMaximumShots() {
        return maximumShots;
    }

    @Override
    public void act(Field currentField, Field updatedField, List<Actor> newActors) {
        for(int shot = 0; shot < maximumShots; shot++){
            Location where = currentField.randomLocation();
            Actor target = currentField.getObjectAt(where);
            if (target != null && target.isActive()){
                target.setActive(false);
            }
        }

        Location newLocation = updatedField.freeAdjacentLocation(getLocation());
            // Only transfer to the updated field if there was a free location
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            }
    }

    @Override
    public Hunter clone() {
        return new HunterBuilder()
                .maximumShots(this.getMaximumShots())
                .build();
    }

    public static class HunterBuilder {
        private int maximumShots = 10;

        public HunterBuilder maximumShots(int value){
            this.maximumShots = value;
            return this;
        }

        public Hunter build(){
            Hunter hunter = new Hunter();
            hunter.maximumShots = this.maximumShots;
            return hunter;
        }


    }
}
