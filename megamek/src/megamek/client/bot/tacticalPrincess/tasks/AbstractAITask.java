package megamek.client.bot.tacticalPrincess.tasks;

import megamek.client.bot.BotClient;
import megamek.client.bot.princess.Princess;
import megamek.client.bot.tacticalPrincess.AiOrganisation;
import megamek.common.enums.GamePhase;

public abstract class AbstractAITask implements AILanceTask {
    TaskState taskState;
    AiOrganisation org;
    Princess owner;
    TaskResult result;
    GamePhase executionPhase;

    AbstractAITask(AiOrganisation org, Princess owner) {
        taskState = TaskState.WAITING;
        this.org = org;
        this.owner = owner;
    }

    @Override
    public TaskState getTaskState() {
        return taskState;
    }

    @Override
    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    @Override
    public GamePhase getExecutionPhase() {
        return executionPhase;
    }
}

