package megamek.client.bot.tacticalPrincess.lanceGeneration;

import megamek.client.bot.princess.Princess;
import megamek.client.bot.tacticalPrincess.AiOrganisation;
import megamek.common.Entity;

import java.util.List;

public interface LanceGenHeuristic {
     AiOrganisation generateLance(int size, List<Entity> mechs, Princess owner);
}
