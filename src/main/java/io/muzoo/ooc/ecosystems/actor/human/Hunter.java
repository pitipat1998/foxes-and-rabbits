package io.muzoo.ooc.ecosystems.actor.human;

import io.muzoo.ooc.ecosystems.Field;
import io.muzoo.ooc.ecosystems.Location;
import io.muzoo.ooc.ecosystems.actor.Actor;

import java.util.List;

public class Hunter extends Human {

    // maximum number of shot
    private static final int MAXIMUM_SHOTS = 10;

    @Override
    public void act(Field currentField, Field updatedField, List<Actor> newActors) {
        for(int shot = 0; shot < MAXIMUM_SHOTS; shot++){
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


}
