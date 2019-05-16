package io.muzoo.ooc.ecosystems.actor.human;

import io.muzoo.ooc.ecosystems.Location;
import io.muzoo.ooc.ecosystems.actor.Actor;

public abstract class Human implements Actor {

    // Whether the animal is alive or not.
    private boolean alive = true;
    // The human's position
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(int row, int col){
        this.location = new Location(row, col);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean isActive() {
        return alive;
    }

    @Override
    public void setActive(boolean active) {}

    public abstract Human clone();

}
