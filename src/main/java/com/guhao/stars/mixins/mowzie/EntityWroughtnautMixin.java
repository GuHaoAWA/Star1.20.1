/*
package com.guhao.stars.mixins.mowzie;

import com.bobmowzie.mowziesmobs.server.entity.MowzieLLibraryEntity;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import net.corruptdog.cdm.gameasset.CorruptAnimations;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import yesman.epicfight.network.server.SPAnimatorControl;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import static com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut.*;


@Mixin(value = EntityWroughtnaut.class, remap = false)

public class EntityWroughtnautMixin  extends MowzieLLibraryEntity implements Enemy {
    public EntityWroughtnautMixin(EntityType<? extends EntityWroughtnaut> type, Level world) {
        super(type, world);
        xpReward = 30;
        active = false;
        setMaxUpStep(1);
//        rightEyePos = new Vector3d(0, 0, 0);
//        leftEyePos = new Vector3d(0, 0, 0);
//        rightEyeRot = new Vector3d(0, 0, 0);
//        leftEyeRot = new Vector3d(0, 0, 0);

        dropAfterDeathAnim = true;

    }
    @Unique
    public boolean vulnerable;

    */
/**
     * @author
     * @reason
     *//*

    @Overwrite
    public boolean hurt(DamageSource source, float amount) {
        Entity entitySource = source.getDirectEntity();
        if (entitySource != null) {
            if ((!active || getTarget() == null) &&
                    entitySource instanceof LivingEntity &&
                    !(entitySource instanceof Player && ((Player) entitySource).isCreative()) &&
                    !(entitySource instanceof EntityWroughtnaut)) {
                setTarget((LivingEntity) entitySource);
            }
            if (vulnerable) {
                // 将免疫区域从220度减少到180度（受伤区域扩大到180度）
                int immuneArc = 180; // 免疫区域角度（正面180度）
                int vulnerableArc = 180; // 受伤区域角度（背面180度）

                float entityHitAngle = (float) ((Math.atan2(entitySource.getZ() - getZ(), entitySource.getX() - getX()) * (180 / Math.PI) - 90) % 360);
                float entityAttackingAngle = yBodyRot % 360;

                // 规范化角度到 [0, 360)
                if (entityHitAngle < 0) entityHitAngle += 360;
                if (entityAttackingAngle < 0) entityAttackingAngle += 360;

                // 计算相对角度差
                float entityRelativeAngle = entityHitAngle - entityAttackingAngle;

                // 规范化相对角度到 [-180, 180]
                if (entityRelativeAngle > 180) entityRelativeAngle -= 360;
                if (entityRelativeAngle < -180) entityRelativeAngle += 360;

                // 判断是否在免疫区域（正面180度）
                if (Math.abs(entityRelativeAngle) <= immuneArc / 2f) {
                    playSound(MMSounds.ENTITY_WROUGHT_UNDAMAGED.get(), 0.4F, 2);
                    return false;
                }
                // 在受伤区域（背面180度）
                else {
                    setAnimation(NO_ANIMATION);
                    return super.hurt(source, amount);
                }
            } else {
                */
/*playSound(DuelcraftSoundInit.ENTITY_WROUGHT_BLOCK.get(), 1F, 1);*//*

                EpicFightCapabilities.getEntityPatch(entitySource, LivingEntityPatch.class).playAnimationSynchronized(CorruptAnimations.PARRY_BREAK2,-0.1F, SPAnimatorControl::new);
            }
        }
        else if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(source, amount);
        }
        return false;
    }

    */
/*@Override
    public boolean hurt(DamageSource source, float amount) {
        Entity entitySource = source.getEntity();
        if (entitySource != null) {
            if ((!active || getTarget() == null) && entitySource instanceof LivingEntity && !(entitySource instanceof Player && ((Player) entitySource).isCreative()) && !(entitySource instanceof EntityWroughtnaut)) setTarget((LivingEntity) entitySource);
            if (vulnerable) {
                int arc = 220;
                float entityHitAngle = (float) ((Math.atan2(entitySource.getZ() - getZ(), entitySource.getX() - getX()) * (180 / Math.PI) - 90) % 360);
                float entityAttackingAngle = yBodyRot % 360;
                if (entityHitAngle < 0) {
                    entityHitAngle += 360;
                }
                if (entityAttackingAngle < 0) {
                    entityAttackingAngle += 360;
                }
                float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                if ((entityRelativeAngle <= arc / 2f && entityRelativeAngle >= -arc / 2f) || (entityRelativeAngle >= 360 - arc / 2f || entityRelativeAngle <= -arc + 90f / 2f)) {
                    playSound(MMSounds.ENTITY_WROUGHT_UNDAMAGED.get(), 0.4F, 2);
                    return false;
                } else {
                    setAnimation(NO_ANIMATION);
                    return super.hurt(source, amount);
                }
            } else {
                playSound(DuelcraftSoundInit.ENTITY_WROUGHT_BLOCK.get(), 2F, 1);
                Objects.requireNonNull(entitySource.getServer()).getCommands().performPrefixedCommand(
                        entitySource.createCommandSourceStack().withSuppressedOutput().withPermission(4),
                        "indestructible @s play \"cdmoveset:biped/hit/parry_guard_break1\" 0.2 1"
                );
            }
        }
        else if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(source, amount);
        }
        return false;
    }*//*



    @Override
    public Animation[] getAnimations() {
        return duelcraft$ANIMATIONS;
    }
    private static final Animation[] duelcraft$ANIMATIONS = {
            DIE_ANIMATION,
            HURT_ANIMATION,
            ATTACK_ANIMATION,
            ATTACK_TWICE_ANIMATION,
            ATTACK_THRICE_ANIMATION,
            VERTICAL_ATTACK_ANIMATION,
            STOMP_ATTACK_ANIMATION,
            ACTIVATE_ANIMATION,
            DEACTIVATE_ANIMATION
    };
    @Override
    public Animation getHurtAnimation() {
        return HURT_ANIMATION;
    }
    @Override
    public Animation getDeathAnimation() {
        return DIE_ANIMATION;
    }

}

*/
