package io.muzoo.ooc.ecosystems.actor.animal;

import io.muzoo.ooc.ecosystems.Field;
import io.muzoo.ooc.ecosystems.Location;

interface Predator {

    Location findFood(Field field, Location location);

    void incrementHunger();

}
