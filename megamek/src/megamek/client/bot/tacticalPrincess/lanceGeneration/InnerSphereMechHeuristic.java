package megamek.client.bot.tacticalPrincess.lanceGeneration;

import megamek.client.bot.princess.Princess;
import megamek.client.bot.tacticalPrincess.AiOrganisation;
import megamek.common.Entity;
import megamek.common.EntityWeightClass;
import megamek.common.UnitType;

import java.util.Collections;
import java.util.List;

import static megamek.client.bot.tacticalPrincess.lanceGeneration.LanceGenHelper.addToOrg;
import static megamek.client.bot.tacticalPrincess.lanceGeneration.LanceGenHelper.findByWeightClassOrFirst;

public class InnerSphereMechHeuristic implements  LanceGenHeuristic {

    public AiOrganisation generateLance(int size, List<Entity> mechs, Princess owner) {
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
}
