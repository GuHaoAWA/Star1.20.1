/*
package com.guhao.stars.api;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import reascer.wom.animation.attacks.SpecialAttackAnimation;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.TimePairList;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

public class SpecialMoveAttackAnimation extends SpecialAttackAnimation {


    public SpecialMoveAttackAnimation(float convertTime, float antic, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
        super(convertTime, antic, contact, recovery, collider, colliderJoint, path, armature);
    }

    public SpecialMoveAttackAnimation(float convertTime, float antic, float preDelay, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
        super(convertTime, antic, preDelay, contact, recovery, collider, colliderJoint, path, armature);
    }

    public SpecialMoveAttackAnimation(float convertTime, float antic, float contact, float recovery, InteractionHand hand, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
        super(convertTime, antic, contact, recovery, hand, collider, colliderJoint, path, armature);
    }

    public SpecialMoveAttackAnimation(float convertTime, String path, Armature armature, boolean Coordsetter, Phase... phases) {
        super(convertTime, path, armature, Coordsetter, phases);
    }

    public SpecialMoveAttackAnimation(float convertTime, String path, Armature armature, Phase... phases) {
        super(convertTime, path, armature, phases);
    }
    protected void move(LivingEntityPatch<?> entitypatch, DynamicAnimation animation) {

        if (!this.getState(EntityState.MOVEMENT_LOCKED, entitypatch, entitypatch.getAnimator().getPlayerFor(this).getElapsedTime())) return;

        if (this.validateMovement(entitypatch, animation)) {
            if (this.getState(EntityState.INACTION, entitypatch, entitypatch.getAnimator().getPlayerFor(this).getElapsedTime())) {
                LivingEntity livingentity = entitypatch.getOriginal();
                Vec3 vec3 = this.getCoordVector(entitypatch, animation);
                livingentity.move(MoverType.SELF, vec3);
            }

        }
    }


}
*/
