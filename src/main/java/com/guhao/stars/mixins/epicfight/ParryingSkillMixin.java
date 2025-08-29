package com.guhao.stars.mixins.epicfight;

import com.guhao.stars.client.particle.par.SparkParticle;
import com.guhao.stars.efmex.skills.DOTEPassive;
import com.guhao.stars.entity.StarAttributes;
import com.guhao.stars.regirster.Effect;
import com.guhao.stars.regirster.ParticleType;
import com.guhao.stars.units.StarDataUnit;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataKeys;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.skill.guard.ParryingSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;

@Mixin(value = ParryingSkill.class,remap = false,priority = 500)
public class ParryingSkillMixin extends GuardSkill {
    public ParryingSkillMixin(Builder builder) {
        super(builder);
    }
    private HurtEvent.Pre event;
    /**
     * @author
     * @reason
     */
    @Overwrite
    public static GuardSkill.Builder createActiveGuardBuilder() {
        return GuardSkill.createGuardBuilder().addAdvancedGuardMotion(CapabilityItem.WeaponCategories.SWORD, (itemCap, playerpatch) -> itemCap.getStyle(playerpatch) == CapabilityItem.Styles.ONE_HAND ? new StaticAnimation[]{Animations.SWORD_GUARD_ACTIVE_HIT1, Animations.SWORD_GUARD_ACTIVE_HIT2} : new StaticAnimation[]{Animations.SWORD_GUARD_ACTIVE_HIT2, Animations.SWORD_GUARD_ACTIVE_HIT3})
                .addAdvancedGuardMotion(CapabilityItem.WeaponCategories.LONGSWORD, (itemCap, playerpatch) -> new StaticAnimation[]{Animations.LONGSWORD_GUARD_ACTIVE_HIT1, Animations.LONGSWORD_GUARD_ACTIVE_HIT2})
                .addAdvancedGuardMotion(CapabilityItem.WeaponCategories.AXE, (itemCap, playerpatch) -> new StaticAnimation[]{Animations.SWORD_GUARD_ACTIVE_HIT1, Animations.SWORD_GUARD_ACTIVE_HIT2})
                .addAdvancedGuardMotion(CapabilityItem.WeaponCategories.UCHIGATANA, (itemCap, playerpatch) -> new StaticAnimation[]{Animations.SWORD_GUARD_ACTIVE_HIT1, Animations.SWORD_GUARD_ACTIVE_HIT2})
                .addAdvancedGuardMotion(CapabilityItem.WeaponCategories.TACHI, (itemCap, playerpatch) -> new StaticAnimation[]{Animations.LONGSWORD_GUARD_ACTIVE_HIT1, Animations.LONGSWORD_GUARD_ACTIVE_HIT2})
                .addAdvancedGuardMotion(CapabilityItem.WeaponCategories.SPEAR, (itemCap, playerpatch) -> new StaticAnimation[]{Animations.LONGSWORD_GUARD_ACTIVE_HIT1, Animations.LONGSWORD_GUARD_ACTIVE_HIT2});
    }
    @Inject(
            method = "guard",
            at = @At(
                    value = "INVOKE",
                    target = "Lyesman/epicfight/world/capabilities/entitypatch/player/ServerPlayerPatch;playSound(Lnet/minecraft/sounds/SoundEvent;FFF)V",
                    shift = At.Shift.AFTER,
                    ordinal = 0
            )
    )
    public void star$guard(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        DOTEPassive.breakdown(container);
    }
    // 在if块开头注入
    @Inject(
            method = "guard",
            at = @At(
                    value = "INVOKE",
                    target = "Lyesman/epicfight/world/entity/eventlistener/HurtEvent$Pre;setParried(Z)V",
                    ordinal = 0
            )
    )
    private void onSuccessfulParry(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        AdvancedCustomHumanoidMobPatch<?> longpatch = EpicFightCapabilities.getEntityPatch(event.getDamageSource().getEntity(), AdvancedCustomHumanoidMobPatch.class);
        if (longpatch != null) {
            longpatch.setStamina((float) (longpatch.getStamina() - longpatch.getOriginal().getAttribute(StarAttributes.PARRY_STAMINA_LOSE.get()).getValue()));
            if (longpatch.getOriginal().hasEffect(Effect.STA.get())) {
                longpatch.setStamina(longpatch.getStamina() - longpatch.getOriginal().getEffect(Effect.STA.get()).getAmplifier() +1);
            }
        }
        ServerPlayer playerentity = event.getPlayerPatch().getOriginal();
        ParticleType.ALL_SPARK.get().spawnParticleWithArgument((ServerLevel)playerentity.level(), HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, playerentity, event.getDamageSource().getDirectEntity());
    }
    @Inject(
            method = "guard",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    public void star$head_guard(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        EpicFightDamageSource damageSource = StarDataUnit.getEpicFightDamageSources(event.getDamageSource());
        ServerPlayer playerentity = event.getPlayerPatch().getOriginal();
        boolean successParrying = playerentity.tickCount - container.getDataManager().getDataValue(SkillDataKeys.LAST_ACTIVE.get()) < 8;
        if (damageSource != null && !successParrying && StarDataUnit.isNoParry(damageSource.getAnimation())) {
            ci.cancel();
        }
        if (damageSource != null && (StarDataUnit.isNoGuard(damageSource.getAnimation()) || StarDataUnit.isNoDodge(damageSource.getAnimation()))) {
            ci.cancel();
        }
        if (damageSource != null && (StarDataUnit.isNoDodge(damageSource.getAnimation()) || StarDataUnit.isNoDodge(damageSource.getAnimation()))) {
            ci.cancel();
        }
    }
    @Inject(
            method = {"guard(Lyesman/epicfight/skill/SkillContainer;Lyesman/epicfight/world/capabilities/item/CapabilityItem;Lyesman/epicfight/world/entity/eventlistener/HurtEvent$Pre;FFZ)V"},
            at = {@At("HEAD")},
            remap = false
    )
    private void getSuccessParry(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        this.event = event;
    }

    @ModifyVariable(
            method = {"guard(Lyesman/epicfight/skill/SkillContainer;Lyesman/epicfight/world/capabilities/item/CapabilityItem;Lyesman/epicfight/world/entity/eventlistener/HurtEvent$Pre;FFZ)V"},
            at = @At("HEAD"),
            ordinal = 1,
            remap = false
    )
    private float setImpact(float impact) {
        float blockrate = 1.0F - Math.min((float) this.event.getPlayerPatch().getOriginal().getAttributeValue(StarAttributes.BLOCK_RATE.get()) / 100.0F, 0.9F);
        Object var4 = this.event.getDamageSource();
        if (var4 instanceof EpicFightDamageSource epicdamagesource) {
            float k = epicdamagesource.getImpact();
            return this.event.getAmount() * (1.0F + k / 10F) * blockrate;
        } else {
            return this.event.getAmount() / 3.0F * blockrate;
        }
    }
}
