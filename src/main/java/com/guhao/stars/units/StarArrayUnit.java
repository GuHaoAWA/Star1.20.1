package com.guhao.stars.units;

import net.minecraft.world.damagesource.DamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

public record StarArrayUnit() {




    public static EpicFightDamageSource getEpicFightDamageSources(DamageSource damageSource) {
        if (damageSource instanceof EpicFightDamageSource epicfightDamageSource) {
            return epicfightDamageSource;
        } else {
            return null;
        }
    }
}
