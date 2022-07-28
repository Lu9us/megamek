package megamek.client.bot.tacticalPrincess.tasks;

import megamek.client.bot.tacticalPrincess.tasks.state.TaskResult;
import megamek.client.bot.tacticalPrincess.tasks.state.TaskState;
import megamek.common.enums.GamePhase;

public interface AILanceTask {
    void init();
    boolean preCondition();
    void resolvePreCondition();
    void performTask();
    GamePhase[] getExecutionPhases();
    TaskResult checkPostCondition();
    TaskState getTaskState();
    void setTaskState(TaskState state);
    void onFail();
    void onSuccess();
}
