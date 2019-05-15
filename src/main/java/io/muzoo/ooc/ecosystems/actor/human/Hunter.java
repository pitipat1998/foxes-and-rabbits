package io.muzoo.ooc.ecosystems.actor.human;

import io.muzoo.ooc.ecosystems.Field;
import io.muzoo.ooc.ecosystems.actor.Actor;

import java.util.List;

public class Hunter implements Actor {

    @Override
    public void act(Field currentField, Field updatedField, List<Actor> newActors) {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
