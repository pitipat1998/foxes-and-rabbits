package io.muzoo.ooc.ecosystems.actor.human;

import io.muzoo.ooc.ecosystems.Field;
import io.muzoo.ooc.ecosystems.Location;
import io.muzoo.ooc.ecosystems.actor.Actor;

import java.util.List;

public class Hunter implements Actor {

    // maximum number of shot
    private static final int MAXIMUM_SHOTS = 10;

    // The hunter's position
    private Location location;

    @Override
    public void act(Field currentField, Field updatedField, List<Actor> newActors) {
        for(int shot = 0; shot < MAXIMUM_SHOTS; shot++){
            Location where = currentField.randomLocation();
            Actor target = currentField.getObjectAt(where);
            if (target != null && target.isActive()){
                target.setActive(false);
            }
        }

        Location newLocation = updatedField.freeAdjacentLocation(location);
            // Only transfer to the updated field if there was a free location
            if (newLocation != null) {
                location = newLocation;
                updatedField.place(this, newLocation);
            }
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void setActive(boolean active) {

    }
}
