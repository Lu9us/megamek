/**
 * MegaMek - Copyright (C) 2005 Ben Mazur (bmazur@sev.org)
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 */
package megamek.common.weapons.battlearmor;

import megamek.common.weapons.lrms.LRMWeapon;


/**
 * @author Sebastian Brocks
 */
public class ISBALRM5 extends LRMWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 1922843634155860893L;

    /**
     *
     */
    public ISBALRM5() {
        super();
        name = "LRM 5";
        setInternalName("ISBALRM5");
        addLookupName("IS BA LRM-5");
        addLookupName("IS BA LRM 5");
        heat = 2;
        rackSize = 5;
        minimumRange = 6;
        bv = 45;
        cost = 30000;
        tonnage = .300;
        criticals = 4;
        flags = flags.or(F_NO_FIRES).or(F_BA_WEAPON).andNot(F_MECH_WEAPON).andNot(F_TANK_WEAPON).andNot(F_AERO_WEAPON).andNot(F_PROTO_WEAPON);
        rulesRefs = "261, TM";
        techAdvancement.setTechBase(TECH_BASE_IS)
    	.setIntroLevel(false)
    	.setUnofficial(false)
        .setTechRating(RATING_E)
        .setAvailability(RATING_X, RATING_X, RATING_E, RATING_D)
        .setISAdvancement(3055, 3057, 3060, DATE_NONE, DATE_NONE)
        .setISApproximate(true, false, false, false, false)
        .setPrototypeFactions(F_FS)
        .setProductionFactions(F_FS);
    }
}
