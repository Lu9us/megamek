package megamek.client.bot.tacticalPrincess;

import megamek.client.bot.princess.Princess;
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

        List<Entity> mechs =
                entities.stream()
                .filter(entity -> entity.getUnitType() == UnitType.MEK)
                .collect(Collectors.toList());

        List<Entity> tanks =
                entities.stream()
                .filter(
                        entity -> !entity.hasIndirectWeapons()
                                && entity.getUnitType() == UnitType.TANK
                )
                .collect(Collectors.toList());

        entities.stream().filter(Entity::hasIndirectWeapons).forEach(entity -> {
                    battleGroup.battery.addUnit(entity);});

        int lanceCount = mechs.size()/4;
        int tankPlatoonCount = tanks.size()/4;


        for(int i = 0; i < lanceCount; i++) {

            battleGroup.lances.add(generateLance( 4, mechs, owner));
        }

        for(int i = 0; i < tankPlatoonCount; i++) {
            AiOrganisation platoon = new AiOrganisation(owner, UnitType.TANK);
            battleGroup.tankPlatoons.add(platoon);
        }


        return battleGroup;
    }

    private static AiOrganisation generateLance (int size, List<Entity> mechs, Princess owner) {
        AiOrganisation lance = new AiOrganisation(owner, UnitType.MEK);
        Collections.shuffle(mechs);
        Entity leader = findByWeightClassOrFirst(mechs, EntityWeightClass.WEIGHT_HEAVY);
        lance.setLeader(leader);
        addToOrg(leader, lance, mechs);
        addToOrg(findByWeightClassOrFirst(mechs, EntityWeightClass.WEIGHT_MEDIUM), lance, mechs);
        addToOrg(findByWeightClassOrFirst(mechs, EntityWeightClass.WEIGHT_MEDIUM), lance, mechs);
        addToOrg(findByWeightClassOrFirst(mechs, EntityWeightClass.WEIGHT_LIGHT), lance, mechs);
        if(!mechs.isEmpty() && mechs.size() < size) {
            while (!mechs.isEmpty()) {
                addToOrg(findByWeightClassOrFirst(mechs, EntityWeightClass.WEIGHT_MEDIUM), lance, mechs);
            }
        }
        return lance;
    }

    private static void addToOrg(Entity unit, AiOrganisation organisation, List<Entity> units) {
        if(unit == null) {
            return;
        }
        organisation.addUnit(unit);
        units.remove(unit);
    }

    private static Entity findByWeightClass(List<Entity> data, int weightClass) {
        Optional<Entity> mech =  data
                .stream()
                .filter(entity -> entity.getWeightClass() == weightClass)
                .findFirst();
        return mech.orElse(null);
    }

    private static Entity findByWeightClassOrFirst(List<Entity> data, int weightClass) {
        Entity unit = findByWeightClass(data, weightClass);
        if (unit == null && !data.isEmpty()) {
            unit = data.get(0);
        }
        return unit;
    }
}

