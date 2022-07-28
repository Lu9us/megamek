package megamek.client.bot.tacticalPrincess.tasks;

import megamek.client.bot.princess.PathEnumerator;
import megamek.client.bot.princess.Princess;
import megamek.client.bot.tacticalPrincess.AiOrganisation;
import megamek.client.bot.tacticalPrincess.tasks.state.LanceState;
import megamek.client.bot.tacticalPrincess.tasks.state.TaskResult;
import megamek.client.bot.tacticalPrincess.tasks.state.TaskState;
import megamek.common.Coords;
import megamek.common.MovePath;
import megamek.common.enums.GamePhase;
import megamek.common.pathfinder.BoardClusterTracker;

import java.util.Comparator;
import java.util.List;

public class MoveTo extends AbstractAITask {

    private Coords target;
    private PathEnumerator enumerator;
    private BoardClusterTracker clusterTracker;
    private boolean reachedTarget = false;

    private int deltaDistance = 0;
    private int lastDeltaDistance = 0;
    private int retries = 0;
    private final int MAX_RETRIES = 3;



    public MoveTo(AiOrganisation org, Princess owner, Coords target) {
        super(org, owner);
        executionPhases = new GamePhase[]{GamePhase.PREMOVEMENT};
        this.target = target;
        enumerator = new PathEnumerator(owner, owner.getGame());
        clusterTracker = new BoardClusterTracker();
    }

    @Override
    public void init() {


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
        if(org.lanceState == LanceState.CLEAR) {
            org.getUnits().forEach(entity -> {
                clusterTracker.clearMovableAreas();
                clusterTracker.updateMovableAreas(entity);
                enumerator.recalculateMovesFor(entity);
                List<MovePath> movePaths = enumerator.getUnitPaths().get(entity.getId());
                if (!movePaths.isEmpty()) {
                    movePaths.sort(Comparator.comparingInt(path -> path.getFinalCoords().distance(target)));
                    MovePath primaryPath = movePaths.get(0);
                    if (target.distance(primaryPath.getFinalCoords()) < 3 && entity == org.getLeader()) {
                        this.taskState = TaskState.FINISHED;
                        reachedTarget = true;
                        owner.sendChat("Reached target coords: " + target);
                    }
                    int currentDistance = target.distance(entity.getPosition());
                    int movedDistance = target.distance(primaryPath.getFinalCoords());
                    if (movedDistance >= currentDistance) {
                        retries++;
                    }

                    if (retries >= MAX_RETRIES) {
                        this.taskState = TaskState.FINISHED;
                        reachedTarget = false;
                    }
                    lastDeltaDistance = deltaDistance;
                    org.pathMap.put(entity.getId(), primaryPath);

                } else {
                    this.taskState = TaskState.FINISHED;
                    reachedTarget = false;
                }
            });
        }

    }

    @Override
    public TaskResult checkPostCondition() {
        return reachedTarget? TaskResult.SUCCESS : TaskResult.FAILED;
    }

    @Override
    public void onFail() {

    }

    @Override
    public void onSuccess() {

    }
}
