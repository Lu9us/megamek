package megamek.client.bot.tacticalPrincess;

import megamek.client.bot.BotClient;
import megamek.common.Entity;
import megamek.common.Game;
import megamek.common.UnitType;

import java.util.List;
import java.util.stream.Collectors;

public class BattleGroupHelpers {

    public static BattleGroup createBattleGroup(Game game, BotClient owner) {
        BattleGroup battleGroup = new BattleGroup();
        List<Entity> enterties =  owner.getEntitiesOwned();
        List<Entity> mechs =  enterties
                .stream()
                .filter(entity -> entity.getUnitType() == UnitType.MEK)
                .collect(Collectors.toList());
        List<Entity> tanks =  enterties
                .stream()
                .filter(entity -> entity.getUnitType() == UnitType.TANK)
                .collect(Collectors.toList());

        int lanceCount = mechs.size()/4;

        for(int i = 0; i < lanceCount; i++) {
            Lance lance = new Lance();
            battleGroup.lances.add(lance);
        }

        for (int i = 0; i < mechs.size(); i++ ) {
            int lance = i % 4;
            
        }

        return battleGroup;
    }

}
