package megamek.client.bot.tacticalPrincess;

import java.util.ArrayList;
import java.util.List;

public class BattleGroup {
   public List<Lance> lances = new ArrayList<>();
   public ArtilleryBattery battery = new ArtilleryBattery();
   public List<TankPlatoon> tankPlatoons = new ArrayList<>();
   public List<InfantryPlatoon> infantryPlatoons = new ArrayList<>();
}
