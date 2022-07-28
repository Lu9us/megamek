package megamek.client.bot.tacticalPrincess;

import megamek.client.bot.princess.Princess;
import megamek.client.bot.tacticalPrincess.lanceGeneration.InnerSphereMechHeuristic;
import megamek.client.bot.tacticalPrincess.lanceGeneration.LanceGenHeuristic;
import megamek.common.Entity;
import megamek.common.EntityWeightClass;
import megamek.common.UnitType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AiOrganisationHelper {

    public static AiOrganisationController createBattleGroup (Princess owner) {
        AiOrganisationController battleGroup = new AiOrganisationController(owner);
        List<Entity> entities =  owner.getAllEntitiesOwned();
        generateLances(owner, battleGroup, entities, new InnerSphereMechHeuristic(), 4, UnitType.MEK, true);
        generateLances(owner, battleGroup, entities, new InnerSphereMechHeuristic(), 4, UnitType.TANK, false);
        generateLances(owner, battleGroup, entities, new InnerSphereMechHeuristic(), 4, UnitType.AERO, false);
        return battleGroup;
    }

    private static void generateLances(
            Princess owner,
            AiOrganisationController battleGroup,
            List<Entity> entities,
            LanceGenHeuristic heuristic,
            int lanceSize,
            int unitType,
            boolean includeIndirect
    ) {
        List<Entity> filteredEntities =
                entities.stream()
                        .filter(entity -> entity.getUnitType() == unitType)
                        .collect(Collectors.toList());

        entities.stream().filter(Entity::hasIndirectWeapons).forEach(entity -> battleGroup.battery.addUnit(entity));


        int lanceCount = (int)Math.ceil((float)filteredEntities.size()/lanceSize);


        for(int i = 0; i < lanceCount; i++) {
            AiOrganisation lance = heuristic.generateLance(lanceSize, filteredEntities, owner);
            if(!includeIndirect) {
                battleGroup.battery.getUnits().forEach(entity -> lance.removeUnit(entity.getId()));
            }
            if(!lance.getUnits().isEmpty()) {
                lance.setLeader(lance.getUnits().get(0));
                battleGroup.lances.add(lance);
            }
        }
    }
}

