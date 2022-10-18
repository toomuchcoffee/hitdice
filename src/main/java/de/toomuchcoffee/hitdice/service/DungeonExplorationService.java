package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.equipment.Item;
import de.toomuchcoffee.hitdice.domain.world.Direction;
import de.toomuchcoffee.hitdice.domain.world.Dungeon;
import de.toomuchcoffee.hitdice.domain.world.Dungeon.Tile;
import de.toomuchcoffee.hitdice.domain.world.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DungeonExplorationService {

    public Tile move(Dungeon dungeon, Direction direction) {
        Position position = dungeon.move(direction);
        return dungeon.getTile(position);
    }

    public void clear(Dungeon dungeon) {
        dungeon.getTile(dungeon.getPosition()).setOccupant(null);
    }

    public Optional<Item> getTreasure(Dungeon dungeon) {
        return Optional.ofNullable(dungeon.getTile(dungeon.getPosition()).getOccupant())
                .filter(e -> e instanceof Item)
                .map(i -> (Item) i);
    }

}
