package io.muzoo.ooc.ecosystems.actor;

import io.muzoo.ooc.ecosystems.Location;

public interface ActorFactory {

    Actor create(int row, int col);

}
