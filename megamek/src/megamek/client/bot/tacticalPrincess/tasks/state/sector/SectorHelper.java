package megamek.client.bot.tacticalPrincess.tasks.state.sector;

import megamek.client.bot.princess.Princess;
import megamek.common.Board;
import megamek.common.Coords;
import megamek.common.Entity;

import java.util.*;

public class SectorHelper {
    public static Sector getSectorForEntity(Entity entity, Map<Coords, Sector> sectorMap, Princess owner) {
        Coords position = entity.getPosition();
        Set<Coords> coordanites = sectorMap.keySet();
        for(Coords coords: coordanites) {
            if((position.getX() >= coords.getX() && position.getY() >= coords.getY()) &&
                    (position.getX() < coords.getX() + Sector.SECTOR_SIZE && position.getY() < coords.getY() + Sector.SECTOR_SIZE )) {
                owner.sendChat("Found sector for entity: " + entity + " sector: " + sectorMap.get(coords));
                return sectorMap.get(coords);
            }
        }
        return null;
    }

    public static void calculateSectorState(List<Entity> friendlyUnits, List<Entity> enemyUnits, Map<Coords, Sector> sectorMap, Princess owner) {
        sectorMap.forEach(((coords, sector) -> {
            sector.enemyBVPresent = 0;
            sector.friendlyBVPresent = 0;
        }));
        friendlyUnits.stream().forEach(entity -> {
            Objects.requireNonNull(getSectorForEntity(entity, sectorMap, owner)).friendlyBVPresent += entity.getInitialBV();
        });
        enemyUnits.stream().forEach(entity -> {
            Objects.requireNonNull(getSectorForEntity(entity, sectorMap, owner)).enemyBVPresent += entity.getInitialBV();
        });
    }

    public static Map<Coords, Sector> constructSectors(Princess owner) {
        Board board = owner.getGame().getBoard();
        Map<Coords, Sector> aiSectors = new HashMap<>();
        for (int i = 0; i < board.getWidth(); i += Sector.SECTOR_SIZE) {
            for (int j = 0; j < board.getHeight(); j += Sector.SECTOR_SIZE) {

                int xOffset = i + Sector.SECTOR_SIZE > board.getWidth() ?
                        board.getWidth() : i + Sector.SECTOR_SIZE;

                int yOffset = j + Sector.SECTOR_SIZE > board.getHeight() ?
                        board.getHeight() : j + Sector.SECTOR_SIZE;

                Sector sector = new Sector(i,j, xOffset, yOffset);
                owner.sendChat(String.format("new Sector:  x: %d y: %d xEnd: %d yEnd %d", i, j, xOffset, yOffset));
                aiSectors.put(new Coords(i,j), sector);
            }
        }
        return aiSectors;
    }
}
