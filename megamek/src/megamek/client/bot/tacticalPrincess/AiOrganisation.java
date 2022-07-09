package megamek.client.bot.tacticalPrincess;

import megamek.client.bot.BotClient;
import megamek.client.bot.princess.Princess;
import megamek.client.bot.tacticalPrincess.tasks.*;
import megamek.common.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AiOrganisation {
    private List<Entity> units = new ArrayList<>();
    private List<AILanceTask> tasks = new ArrayList<>();
    private long unitType;
    private Entity leader;

    public AiOrganisation (Princess owner, long unitType) {
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

    public void addTask(AILanceTask task, TaskState state) {
        this.tasks.add(task);
        task.iniit();
        task.setTaskState(state);
    }

    public long getUnitType() {
        return unitType;
    }
}
