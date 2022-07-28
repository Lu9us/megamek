package megamek.client.bot.tacticalPrincess.tasks;

import megamek.client.bot.princess.Princess;
import megamek.client.bot.tacticalPrincess.AiOrganisation;
import megamek.client.bot.tacticalPrincess.tasks.state.TaskResult;
import megamek.client.bot.tacticalPrincess.tasks.state.TaskState;
import megamek.common.Coords;
import megamek.common.Entity;
import megamek.common.Hex;
import megamek.common.enums.GamePhase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatrolTask extends AbstractAITask{

    PatrolTask(AiOrganisation org, Princess owner) {
        super(org, owner);
        executionPhases = new GamePhase[]{GamePhase.PREFIRING};

    }
    TaskResult result;
    Hex target;

    private static final int MAX_PATROL_DIST = 40;
    private static final int MIN_PATROL_DIST = 15;

    @Override
    public void init() {

    }

    @Override
    public boolean preCondition() {
        return org.getUnits().stream().noneMatch(Entity::isImmobile);
    }

    @Override
    public void resolvePreCondition() {

    }

    public int compareHexes(Entity entity, Hex hex1, Hex hex2) {
        int hex1Value = hex1.depth() + entity.getPosition().distance(hex1.getCoords());
        int hex2Value = hex2.depth() + entity.getPosition().distance(hex2.getCoords());
        return Integer.compare(hex1Value, hex2Value);
    }

    @Override
    public void performTask() {
        //We want to start patroling towards the enemy

       if (org.getLeader() != null && org.getLeader().isDeployed()) {
          List<Coords> potentialPatrolCoords =  org.getLeader().getPosition().allAtDistanceOrLess(MAX_PATROL_DIST);
          List<Hex> patrolHexs = new ArrayList<>();
           potentialPatrolCoords.forEach(coord -> {
               Hex hex = owner.getBoard().getHex(coord);
               if( hex != null && org.getLeader().getPosition().distance(coord) >= MIN_PATROL_DIST ) {
                   patrolHexs.add(hex);
               }
           });
           patrolHexs.sort((hex1, hex2) -> compareHexes(org.getLeader(), hex1, hex2));
           Collections.reverse(patrolHexs);
           target = patrolHexs.stream().findFirst().orElse(null);

           if(target == null) {
               result = TaskResult.FAILED;
               taskState = TaskState.FINISHED;
               return;
           }
       } else {
           result = TaskResult.FAILED;
           taskState = TaskState.FINISHED;
       }
}

    @Override
    public TaskResult checkPostCondition() {
        return result;
    }

    @Override
    public void onFail() {

    }

    @Override
    public void onSuccess() {

    }
}
