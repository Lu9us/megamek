package megamek.client.bot.tacticalPrincess.tasks;

import megamek.client.bot.princess.Princess;
import megamek.client.bot.tacticalPrincess.AiOrganisation;
import megamek.client.bot.tacticalPrincess.tasks.state.TaskResult;
import megamek.common.Coords;
import megamek.common.enums.GamePhase;

public class TopLevelTask extends AbstractAITask{

    public TopLevelTask(AiOrganisation org, Princess owner) {
        super(org, owner);
        executionPhases = new GamePhase[]{
                GamePhase.INITIATIVE_REPORT,
                GamePhase.PREFIRING,
                GamePhase.PREMOVEMENT
        };

    }

    @Override
    public void init() {

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

        if(org.getTasks().size() < 2) {
            org.addTask(new PatrolTask(org, owner));
        }
        if(org.getTasks().stream().noneMatch(task -> task instanceof MoveTo)) {
            org.addTask(new MoveTo(org, owner, new Coords(owner.getGame().getBoard().getWidth(), owner.getGame().getBoard().getHeight())));
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
