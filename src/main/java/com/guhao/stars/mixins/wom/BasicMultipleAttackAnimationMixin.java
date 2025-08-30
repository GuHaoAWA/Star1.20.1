/*
package com.guhao.stars.mixins.wom;

import com.guhao.stars.units.StarDataUnit;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import reascer.wom.animation.attacks.BasicMultipleAttackAnimation;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@Mixin(value = BasicMultipleAttackAnimation.class,remap = false)
public class BasicMultipleAttackAnimationMixin extends AttackAnimation {

    public BasicMultipleAttackAnimationMixin(float convertTime, float antic, float preDelay, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
        super(convertTime, antic, preDelay, contact, recovery, collider, colliderJoint, path, armature);
    }

    */
/**
     * @author
     * @reason
     *//*

    @Overwrite
    public Vec3 getCoordVector(LivingEntityPatch<?> entitypatch, DynamicAnimation dynamicAnimation) {
        Vec3 vec3 = super.getCoordVector(entitypatch, dynamicAnimation);
        if (entitypatch.isLogicalClient()) {
            LocalPlayerPatch localEntityPatch = (LocalPlayerPatch) entitypatch;
            if (localEntityPatch.isTargetLockedOn() && entitypatch.getAnimator().getPlayerFor(null).getAnimation() instanceof StaticAnimation staticAnimation && StarDataUnit.isLockOff(staticAnimation)) {
                localEntityPatch.setLockOn(true);
            } else {
                localEntityPatch.setLockOn(false);
            }
        }
        if (entitypatch.shouldBlockMoving() && this.getProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE).orElse(false)) {
            vec3 = vec3.scale(0.0);
        }
        return vec3;
    }
}
*/
