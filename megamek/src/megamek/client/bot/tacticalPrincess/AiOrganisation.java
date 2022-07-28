package megamek.client.bot.tacticalPrincess;

import megamek.client.bot.princess.FiringPlan;
import megamek.client.bot.princess.Princess;
import megamek.client.bot.tacticalPrincess.tasks.*;
import megamek.client.bot.tacticalPrincess.tasks.state.LanceState;
import megamek.client.bot.tacticalPrincess.tasks.state.Stance;
import megamek.client.bot.tacticalPrincess.tasks.state.TaskState;
import megamek.client.bot.ui.swing.TacticalPrincessDebugWindow;
import megamek.common.Entity;
import megamek.common.MovePath;
import megamek.common.Targetable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AiOrganisation {
    private List<Entity> units = new ArrayList<>();
    private List<AILanceTask> tasks = new ArrayList<>();
    private List<AILanceTask> newTasks = new ArrayList<>();
    private long unitType;
    private Entity leader;
    public LanceState lanceState;
    public Stance lanceStance;
    public Map<Integer, MovePath> pathMap = new HashMap<>();
    public Map<Integer, FiringPlan> firingPlanMap = new HashMap<>();
    public Map<Targetable, Integer> heatSpentOnTarget = new HashMap();

    public AiOrganisation (Princess owner, long unitType) {
        lanceState = LanceState.CLEAR;
        lanceStance = Stance.OFFENSIVE;
        this.unitType = unitType;
        addTask(new TopLevelTask(this, owner), TaskState.ACTIVE);
        addTask(new EngageEnemies(this, owner), TaskState.ACTIVE);
    }

    public List<Entity> getUnits() {
        return units;
    }

    public void addUnit(Entity unit) {
        units.add(unit);
    }

    public Entity getLeader() {
        return leader;
    }

    public void setLeader(Entity leader) {
        this.leader = leader;
    }

    public void  removeUnit(int id) {
        Entity removeEntity = units
                .stream()
                .filter(entity -> entity.getId() == id)
                .findFirst()
                .orElse(null);
        if (removeEntity != null) {
            units.remove(removeEntity);
            if(leader == removeEntity) {
                leader = null;
            }
        }
    }

    public List<AILanceTask> currentTasks() {
        return tasks.stream().filter(aiLanceTask ->
                aiLanceTask.getTaskState() == TaskState.ACTIVE).collect(Collectors.toList());
    }

    public List<AILanceTask> getTasks() {
        return tasks;
    }

    public void addTask(AILanceTask task) {
        addTask(task, TaskState.WAITING);
    }

    public void insertNewTasks() {
        tasks.addAll(newTasks);
        newTasks.clear();
    }

    public void addTask(AILanceTask task, TaskState state) {
        this.newTasks.add(task);
        task.init();
        task.setTaskState(state);
    }

    public long getUnitType() {
        return unitType;
    }
}
