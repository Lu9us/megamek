package megamek.client.bot.tacticalPrincess;

import megamek.client.bot.princess.Princess;
import megamek.common.Coords;
import megamek.common.UnitType;

import java.util.ArrayList;
import java.util.List;

public class ArtilleryBattery extends AiOrganisation {
    private List<Coords> targets = new ArrayList<>();

    public ArtilleryBattery(Princess owner) {
        super(owner, UnitType.TANK);
    }

    public List<Coords> getTargets() {
        return targets;
    }

    public void addTarget(Coords target) {
        this.targets.add(target);
    }
}
