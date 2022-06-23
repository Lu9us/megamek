package megamek.client.bot.tacticalPrincess;

import megamek.common.Coords;

import java.util.ArrayList;
import java.util.List;

public class ArtilleryBattery extends AiOrganisation {
    private List<Coords> targets = new ArrayList<>();

    public List<Coords> getTargets() {
        return targets;
    }

    public void addTarget(Coords target) {
        this.targets.add(target);
    }
}
