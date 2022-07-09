package megamek.client.bot.tacticalPrincess.tasks;

import megamek.client.bot.princess.Princess;
import megamek.client.bot.tacticalPrincess.AiOrganisation;
import megamek.common.enums.GamePhase;

public class TopLevelTask extends AbstractAITask{

    public TopLevelTask(AiOrganisation org, Princess owner) {
        super(org, owner);
        executionPhase = GamePhase.INITIATIVE_REPORT;
    }

    @Override
    public void iniit() {

    }

    @Override
    public boolean preCondition() {
        //this task should always be able to performed
        return true;
    }

    @Override
    public void resolvePreCondition() {

    }

    @Override
    public void performTask() {
        // if a lance enters this task they haven't been assigned any other so we should give them
        // to do
        if(org.getTasks().size() < 2) {
            org.addTask(new PatrolTask(org, owner));
        }
    }

    @Override
    public TaskResult checkPostCondition() {
        return TaskResult.FAILED;
    }

    @Override
    public void onFail() {

    }

    @Override
    public void onSuccess() {

    }
}
