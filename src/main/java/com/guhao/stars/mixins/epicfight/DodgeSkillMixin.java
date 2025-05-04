package com.guhao.stars.mixins.epicfight;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.skill.dodge.DodgeSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = DodgeSkill.class,remap = false)
public class DodgeSkillMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean isExecutableState(PlayerPatch<?> executer) {
        EntityState playerState = executer.getEntityState();
        return !executer.isInAir() && playerState.canUseSkill() && !executer.getOriginal().onClimbable() && executer.getOriginal().getVehicle() == null;
    }
}
