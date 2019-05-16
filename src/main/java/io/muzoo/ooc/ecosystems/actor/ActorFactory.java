package io.muzoo.ooc.ecosystems.actor;

import io.muzoo.ooc.ecosystems.Location;

public interface ActorFactory {

    Actor create(String type, Location loc);

    Actor createRandom(int row, int col);

}
