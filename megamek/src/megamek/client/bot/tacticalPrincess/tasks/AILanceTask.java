package megamek.client.bot.tacticalPrincess.tasks;

import megamek.common.enums.GamePhase;

public interface AILanceTask {
    void iniit();
    boolean preCondition();
    void resolvePreCondition();
    void performTask();
    GamePhase getExecutionPhase();
    TaskResult checkPostCondition();
    TaskState getTaskState();
    void setTaskState(TaskState state);
    void onFail();
    void onSuccess();
}
