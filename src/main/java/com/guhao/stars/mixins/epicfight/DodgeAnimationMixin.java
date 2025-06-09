package com.guhao.stars.mixins.epicfight;

import com.guhao.stars.units.StarArrayUnit;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.DodgeAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.world.damagesource.EpicFightDamageType;

import java.util.function.Function;

@Mixin(value = DodgeAnimation.class,remap = false)
public abstract class DodgeAnimationMixin extends ActionAnimation {
    public DodgeAnimationMixin(float convertTime, String path, Armature armature) {
        super(convertTime, path, armature);
    }
    @Unique
    private static final Function<DamageSource, AttackResult.ResultType> star$DODGEABLE_SOURCE_VALIDATOR = (damagesource) -> {
        if (StarArrayUnit.getEpicFightDamageSources(damagesource) != null) {
            return damagesource.getEntity() != null && !damagesource.is(DamageTypeTags.IS_EXPLOSION) && StarArrayUnit.isNoDodge(StarArrayUnit.getEpicFightDamageSources(damagesource).getAnimation()) && !damagesource.is(DamageTypes.MAGIC) && !damagesource.is(DamageTypeTags.BYPASSES_ARMOR) && !damagesource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !damagesource.is(EpicFightDamageType.BYPASS_DODGE) ? AttackResult.ResultType.MISSED : AttackResult.ResultType.SUCCESS;
        }
        return damagesource.getEntity() != null && !damagesource.is(DamageTypeTags.IS_EXPLOSION) && !damagesource.is(DamageTypes.MAGIC) && !damagesource.is(DamageTypeTags.BYPASSES_ARMOR) && !damagesource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !damagesource.is(EpicFightDamageType.BYPASS_DODGE) ? AttackResult.ResultType.MISSED : AttackResult.ResultType.SUCCESS;
    };



    @Inject(method = "<init>(FFLjava/lang/String;FFLyesman/epicfight/api/model/Armature;)V",at = @At("TAIL"))
    public void setStar$DODGEABLE_SOURCE_VALIDATOR(float convertTime, float delayTime, String path, float width, float height, Armature armature, CallbackInfo ci) {
        this.stateSpectrumBlueprint.clear().newTimePair(0.0F, delayTime).addState(EntityState.TURNING_LOCKED, true).addState(EntityState.MOVEMENT_LOCKED, true).addState(EntityState.UPDATE_LIVING_MOTION, false).addState(EntityState.CAN_BASIC_ATTACK, false).addState(EntityState.CAN_SKILL_EXECUTION, false).addState(EntityState.INACTION, true).newTimePair(0.0F, Float.MAX_VALUE).addState(EntityState.ATTACK_RESULT, star$DODGEABLE_SOURCE_VALIDATOR);
    }
}
