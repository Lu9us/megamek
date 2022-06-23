package megamek.client.bot.tacticalPrincess;

import megamek.common.Entity;

import java.util.ArrayList;
import java.util.List;

public class AiOrganisation {
    private List<Entity> units = new ArrayList<>();

    public List<Entity> getUnits() {
        return units;
    }

    public void addUnit(Entity unit) {
        units.add(unit);
    }
}
