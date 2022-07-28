package megamek.client.bot.tacticalPrincess;

import megamek.client.bot.princess.FiringPlan;
import megamek.client.bot.princess.Princess;
import megamek.client.bot.tacticalPrincess.tasks.AILanceTask;
import megamek.client.bot.tacticalPrincess.tasks.state.sector.Sector;
import megamek.client.bot.tacticalPrincess.tasks.state.TaskState;
import megamek.client.bot.tacticalPrincess.tasks.state.sector.SectorHelper;
import megamek.client.bot.ui.swing.TacticalPrincessDebugWindow;
import megamek.common.*;
import megamek.common.enums.GamePhase;
import org.apache.logging.log4j.LogManager;

import java.util.*;

import static megamek.client.bot.tacticalPrincess.tasks.state.sector.SectorHelper.constructSectors;

public class AiOrganisationController {
    private final TacticalPrincessDebugWindow debugWindow;
    private GamePhase lastRunInPhase;

    public Map<Coords, Sector> aiSectors = new HashMap<>();
    public List<AiOrganisation> lances = new ArrayList<>();
    public ArtilleryBattery battery;
    public List<AiOrganisation> tankPlatoons = new ArrayList<>();
    public List<AiOrganisation> infantryPlatoons = new ArrayList<>();

    private Princess owner;

    public AiOrganisationController(Princess owner) {
        this.owner = owner;
        battery = new ArtilleryBattery(owner);
        aiSectors = constructSectors(owner);
        this.debugWindow = new TacticalPrincessDebugWindow(owner);

    }

    public void clearTransientData(GamePhase phase) {
            lances.forEach(AiOrganisation::insertNewTasks);
            switch (phase) {
                case MOVEMENT:
                    for (AiOrganisation lance : lances) {
                        lance.firingPlanMap.clear();
                        lance.heatSpentOnTarget.clear();
                    }
                    break;

                case FIRING:
                    for (AiOrganisation lance : lances) {
                        lance.pathMap.clear();
                    }

                default:
            }
    }



    public boolean hasFiringPlanForShooter(Entity entity) {
        return entityIsInOrg(entity) && getOrgForEntity(entity).firingPlanMap.containsKey(entity.getId());
    }

    public boolean hasPathForEntity(Entity entity) {
        return entityIsInOrg(entity) && getOrgForEntity(entity).pathMap.containsKey(entity.getId());
    }

    public FiringPlan getFiringPlanForShooter(Entity entity) {
        return getOrgForEntity(entity).firingPlanMap.get(entity.getId());
    }

    public AiOrganisation getOrgForEntity(Entity entity) {
        if (entity.getUnitType() == UnitType.MEK) {
            return lances
                    .stream()
                    .filter(lance -> lance.getUnits().stream().anyMatch(entity1 -> entity.getId() == entity1.getId()))
                    .findFirst()
                    .orElse(null);
        } else if (entity.getUnitType() == UnitType.TANK) {
            return tankPlatoons.stream()
                    .filter(lance -> lance.getUnits().stream().anyMatch(entity1 -> entity.getId() == entity1.getId()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public boolean entityIsInOrg(Entity entity) {
        return getOrgForEntity(entity) != null;
    }

    public void updateEntityForOrg(Entity entity) {
        if(entityIsInOrg(entity)) {
            AiOrganisation org = getOrgForEntity(entity);
            org.removeUnit(entity.getId());
            if(entity.isDestroyed()) {
                if(org.getUnits().size() == 0) {
                    this.lances.remove(org);
                    return;
                }
                if(org.getLeader() == null) {
                    org.setLeader(org.getUnits().get(0));
                }
            } else {
                org.addUnit(entity);
                if(org.getLeader() == null) {
                    org.setLeader(entity);
                }
            }
        }
    }

    public void processAiTasks() {
        try {
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
                                owner.sendChat("Task: " + task + "failed");
                                task.onFail();
                                break;
                            case SUCCESS:
                                owner.sendChat("Task: " + task + "success");
                                task.onSuccess();
                                break;
                        }
                        iterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            owner.doAlertDialog("exception",  e.toString());
        }

    }

    public synchronized void executeAiTasks(GamePhase gamePhase) {
        try {
            SectorHelper.calculateSectorState(owner.getFriendEntities(), owner.getEnemyEntities(), aiSectors, owner);
            debugWindow.redraw();
            lances.forEach(lance -> {
                lance.getTasks().stream().filter(task ->
                        Arrays.stream(task.getExecutionPhases()).anyMatch( phase -> phase == gamePhase)
                                && task.getTaskState() == TaskState.ACTIVE)
                        .forEach(aiLanceTask -> {
                    owner.sendChat("Executing task: " + aiLanceTask.toString() + " for lance: " + lance.toString());
                    aiLanceTask.performTask();
                });
            });
            clearTransientData(gamePhase);
        } catch (Exception e) {
            owner.doAlertDialog("exception", e.toString());
            LogManager.getLogger().debug(e.toString());
        }
    }

}
