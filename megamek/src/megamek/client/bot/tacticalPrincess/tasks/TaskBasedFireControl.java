package megamek.client.bot.tacticalPrincess.tasks;

import megamek.client.bot.princess.*;
import megamek.client.bot.tacticalPrincess.AiOrganisation;
import megamek.common.Entity;
import megamek.common.Game;
import megamek.common.Mounted;
import megamek.common.Targetable;

import java.util.Map;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class TaskBasedFireControl extends MultiTargetFireControl {
    /**
     * Constructor
     *
     * @param owningPrincess The {@link Princess} bot that utilizes this this class for computing firing solutions.
     */
    AiOrganisation org;

    public TaskBasedFireControl(Princess owningPrincess, AiOrganisation organisation) {
        super(owningPrincess);
        org = organisation;
    }


    @Override
    protected void calculateUtility(final FiringPlan firingPlan, final int overheatTolerance, final boolean shooterIsAero) {

        double expectedDamage = firingPlan.getExpectedDamage();
        double utility = 0;
        double modifier = 1;
        utility += DAMAGE_UTILITY * expectedDamage;
        utility += CRITICAL_UTILITY * firingPlan.getExpectedCriticals();
        utility += KILL_UTILITY * firingPlan.getKillProbability();

      utility +=  firingPlan.stream().flatMapToDouble( weaponFireInfo -> {
            Targetable target = weaponFireInfo.getTarget();
            Entity targetEntity = owner.getGame().getEntity(target.getTargetId());
            double totalUtil = 0;
            // We want to target the most functional enemies first,
            if (targetEntity != null) {
                int totalWeapons = targetEntity.getWeaponList().size();
                int bv = targetEntity.getInitialBV();
                int functionalWeapons = (int) targetEntity.getWeaponList().stream().filter(Mounted::canFire).count();
                double bvPerWeapon = (bv / totalWeapons) * functionalWeapons;
                bvPerWeapon = bvPerWeapon / 10;
                totalUtil = +bvPerWeapon;
            }
            int heatSpentOnTarget = 0;
            if (org.heatSpentOnTarget.containsKey(target)) {
                heatSpentOnTarget = org.heatSpentOnTarget.get(target);
            }
            // don't focus the lances fire on one unit
            totalUtil -= heatSpentOnTarget * 10;
            return DoubleStream.of(totalUtil);
        }).sum();

        int overheat = 0;
        if (firingPlan.getHeat() > overheatTolerance) {
            overheat = firingPlan.getHeat() - overheatTolerance;
        }
        utility -= (shooterIsAero ? OVERHEAT_DISUTILITY_AERO : OVERHEAT_DISUTILITY) * overheat;
        utility *= modifier;
        // eliminated ejected pilot disutility, as it's superflous - we will ignore ejected mechwarriors altogether.
        firingPlan.setUtility(utility);

    }

    @Override
    public FiringPlan getBestFiringPlan(final Entity shooter, final IHonorUtil honorUtil, final Game game, final Map<Mounted, Double> ammoConservation) {
        FiringPlan bestFiringPlan = super.getBestFiringPlan(shooter,honorUtil,game,ammoConservation);
        bestFiringPlan.forEach(weaponFireInfo -> {
            if (org.heatSpentOnTarget.containsKey(weaponFireInfo.getTarget())) {
                org.heatSpentOnTarget.put(weaponFireInfo.getTarget(), org.heatSpentOnTarget.get(weaponFireInfo.getTarget()) + weaponFireInfo.getHeat());
            } else {
                org.heatSpentOnTarget.put(weaponFireInfo.getTarget(), weaponFireInfo.getHeat());
            }
        });
        return  bestFiringPlan;
    }
}
