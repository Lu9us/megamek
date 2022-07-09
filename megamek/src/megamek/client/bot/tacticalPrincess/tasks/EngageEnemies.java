package megamek.client.bot.tacticalPrincess.tasks;

import megamek.client.bot.princess.FireControl;
import megamek.client.bot.princess.FiringPlan;
import megamek.client.bot.princess.FiringPlanCalculationParameters;
import megamek.client.bot.princess.Princess;
import megamek.client.bot.tacticalPrincess.AiOrganisation;
import megamek.common.Entity;
import megamek.common.enums.GamePhase;

import java.util.ArrayList;
import java.util.List;

public class EngageEnemies extends AbstractAITask{
    FireControl fireControl;
    public EngageEnemies(AiOrganisation org, Princess owner) {
        super(org, owner);
        executionPhase = GamePhase.PREFIRING;
    }

    @Override
    public void iniit() {
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
        List<Entity> enemyForces = owner.getEnemyEntities();

        org.getUnits().forEach(
                entity -> {
                 FiringPlanCalculationParameters params =  new FiringPlanCalculationParameters
                         .Builder()
                         .setShooter(entity)
                         .build();
                FiringPlan plan = fireControl.determineBestFiringPlan(params);

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
