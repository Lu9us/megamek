package megamek.client.bot.tacticalPrincess.tasks;

import megamek.client.bot.princess.FireControl;
import megamek.client.bot.princess.FiringPlan;
import megamek.client.bot.princess.FiringPlanCalculationParameters;
import megamek.client.bot.princess.Princess;
import megamek.client.bot.tacticalPrincess.AiOrganisation;
import megamek.client.bot.tacticalPrincess.tasks.state.TaskResult;
import megamek.common.enums.GamePhase;

public class EngageEnemies extends AbstractAITask{
    FireControl fireControl;
    public EngageEnemies(AiOrganisation org, Princess owner) {
        super(org, owner);
        executionPhases = new GamePhase[]{GamePhase.PREFIRING};
    }

    @Override
    public void init() {
        fireControl = new TaskBasedFireControl(owner, org);
    }

    @Override
    public boolean preCondition() {
        return true;
    }

    @Override
    public void resolvePreCondition() {

    }

    @Override
    public void performTask() {

        org.getUnits().forEach(
                entity -> {
                 FiringPlanCalculationParameters params =  new FiringPlanCalculationParameters
                         .Builder()
                         .setShooter(entity)
                         .build();
                FiringPlan plan = fireControl
                        .getBestFiringPlan(entity, owner.getHonorUtil(), owner.getGame() ,owner.calcAmmoConservation(entity));
                org.firingPlanMap.put(entity.getId(), plan);
                }
        );

    }

    @Override
    public TaskResult checkPostCondition() {
        return null;
    }

    @Override
    public void onFail() {

    }

    @Override
    public void onSuccess() {

    }
}
