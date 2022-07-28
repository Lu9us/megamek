package megamek.client.bot.tacticalPrincess.lanceGeneration;

import megamek.client.bot.tacticalPrincess.AiOrganisation;
import megamek.common.Entity;

import java.util.List;
import java.util.Optional;

public class LanceGenHelper {
    public static void addToOrg(Entity unit, AiOrganisation organisation, List<Entity> units) {
        if(unit == null) {
            return;
        }
        organisation.addUnit(unit);
        units.remove(unit);
    }

    public static Entity findByWeightClass(List<Entity> data, int weightClass) {
        Optional<Entity> mech =  data
                .stream()
                .filter(entity -> entity.getWeightClass() == weightClass)
                .findFirst();
        return mech.orElse(null);
    }

    public static Entity findByWeightClassOrFirst(List<Entity> data, int weightClass) {
        Entity unit = findByWeightClass(data, weightClass);
        if (unit == null && !data.isEmpty()) {
            unit = data.get(0);
        }
        return unit;
    }
}
