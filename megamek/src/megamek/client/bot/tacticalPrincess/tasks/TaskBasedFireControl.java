package megamek.client.bot.tacticalPrincess.tasks;

import megamek.client.bot.princess.FireControl;
import megamek.client.bot.princess.FiringPlan;
import megamek.client.bot.princess.IHonorUtil;
import megamek.client.bot.princess.Princess;
import megamek.client.bot.tacticalPrincess.AiOrganisation;
import megamek.common.Entity;
import megamek.common.Game;
import megamek.common.Mounted;
import megamek.common.Targetable;

import java.util.Map;

public class TaskBasedFireControl extends FireControl {
    /**
     * Constructor
     *
     * @param owningPrincess The {@link Princess} bot that utilizes this this class for computing firing solutions.
     */
    public TaskBasedFireControl(Princess owningPrincess, AiOrganisation organisation) {
        super(owningPrincess);
    }

    @Override
    public FiringPlan getBestFiringPlan(final Entity shooter,
                                        final IHonorUtil honorUtil,
                                        final Game game,
                                        final Map<Mounted, Double> ammoConservation) {
        

    return new FiringPlan();
    }


        @Override
    protected void calculateUtility(final FiringPlan firingPlan,
                          final int overheatTolerance,
                          final boolean shooterIsAero) {
        Targetable target = firingPlan.getTarget();
        int overheat = 0;
        if (firingPlan.getHeat() > overheatTolerance) {
            overheat = firingPlan.getHeat() - overheatTolerance;
        }

    }
}
