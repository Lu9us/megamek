package megamek.client.bot.tacticalPrincess;

import megamek.client.bot.BotClient;
import megamek.client.bot.princess.FiringPlan;
import megamek.client.bot.princess.Princess;
import megamek.client.bot.tacticalPrincess.tasks.AILanceTask;
import megamek.client.bot.tacticalPrincess.tasks.TaskState;
import megamek.common.Entity;
import megamek.common.MovePath;
import megamek.common.Targetable;
import megamek.common.UnitType;
import megamek.common.enums.GamePhase;

import java.util.*;

public class AiOrganisationController {
   public List<AiOrganisation> lances = new ArrayList<>();
   public ArtilleryBattery battery;
   public List<AiOrganisation> tankPlatoons = new ArrayList<>();
   public List<AiOrganisation> infantryPlatoons = new ArrayList<>();
   public Map<Entity, MovePath> pathMap = new HashMap<>();
   public Map<Entity, FiringPlan> firingPlanMap = new HashMap<>();
   public Map<Targetable, Integer> heatSpentOnTarget = new HashMap();

   public AiOrganisationController(Princess owner) {
        battery = new ArtilleryBattery(owner);
   }

   public void clearTransientData(GamePhase phase) {
       switch (phase) {
           case FIRING:
               firingPlanMap.clear();
               heatSpentOnTarget.clear();
           case MOVEMENT:
               pathMap.clear();
           default:
       }

   }

   public AiOrganisation getOrgForEntity(Entity entity) {
       if(entity.getUnitType() == UnitType.MEK) {
           return lances.stream()
                   .filter(lance -> lance.getUnits()
                           .contains(entity))
                   .findFirst()
                   .orElse(null);
       } else if (entity.getUnitType() == UnitType.TANK) {
           return lances.stream()
                   .filter(lance -> lance.getUnits()
                           .contains(entity))
                   .findFirst()
                   .orElse(null);
       }
       return null;
   }

   public void processAiTasks() {
       for (AiOrganisation lance : lances) {
          List<AILanceTask> tasks = lance.getTasks();

           for (Iterator<AILanceTask> iterator = tasks.iterator(); iterator.hasNext(); ) {
               AILanceTask task = iterator.next();
               if (task.preCondition() && task.getTaskState() == TaskState.WAITING) {
                   task.setTaskState(TaskState.ACTIVE);
               } else if (!task.preCondition() && task.getTaskState() == TaskState.WAITING) {
                   task.resolvePreCondition();
               }

               if (task.getTaskState() == TaskState.FINISHED) {
                   switch (task.checkPostCondition()) {
                       case FAILED:
                           task.onFail();
                           break;
                       case SUCCESS:
                           task.onSuccess();
                           break;
                   }
                   iterator.remove();
               }
           }
       }
   }

   public void executeAiTasks(GamePhase gamePhase) {
       if(gamePhase == GamePhase.FIRING_REPORT) {
           firingPlanMap.clear();
       }
       if(gamePhase == GamePhase.MOVEMENT_REPORT) {
           pathMap.clear();
       }

       lances.forEach(lance -> {
           lance.getTasks()
                   .stream()
                   .filter(
                           task -> task.getExecutionPhase() == gamePhase
                                   && task.getTaskState() == TaskState.ACTIVE)
                   .forEach(AILanceTask::performTask);
       });
       clearTransientData(gamePhase);
   }

}
