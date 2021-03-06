package io.muzoo.ooc.ecosystems.actor;

import io.muzoo.ooc.ecosystems.Field;

import java.util.List;

public interface Actor {

    /**
     * Perform the actor's regular behavior
     * @param currentField current field
     * @param updatedField new filed
     * @param newActors list of new actors
     */
    void act(Field currentField, Field updatedField, List<Actor> newActors);

    /**
     * Is the actor still active?
     * @return true if still, false if not
     */
    boolean isActive();

    /**
     *  set activeness of the actor
     * @param active activeness
     */
    void setActive(boolean active);
}
