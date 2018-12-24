package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.World;
import org.springframework.stereotype.Service;

@Service
public class DungeonService {

    public World create() {
        return new World(8);
    }
}
