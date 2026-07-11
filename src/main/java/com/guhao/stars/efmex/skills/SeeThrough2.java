package com.guhao.stars.efmex.skills;

import com.guhao.stars.efmex.StarSkillCategories;
import com.guhao.stars.efmex.StarSkillDataKeys;
import com.guhao.stars.entity.StarAttributes;
import com.guhao.stars.regirster.StarsEffect;
import com.guhao.stars.utils.dangerAnimSystem.AnimationEffectManager;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import com.nameless.indestructible.world.capability.Utils.IAdvancedCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import yesman.epicfight.world.entity.eventlistener.SkillCastEvent;

import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("removal")
// TODO 高级识破  可获得额外的属性附魔
public class SeeThrough2 extends Skill {
    private static final UUID EVENT_UUID = UUID.fromString("4e189a70-e24f-4e6f-b433-ac98b80281c4");
    public SeeThrough2(Builder builder) {
        super(builder);
    }

    public static Builder createSeeThrough2Builder() {
        return (new Builder())
                .setCategory(StarSkillCategories.COUNTER)
                .setActivateType(ActivateType.DURATION)
                .setResource(Resource.NONE);
    }

    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_ATTACK, EVENT_UUID);
    }


    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_ATTACK, EVENT_UUID, (event) -> {
            if(event.getResult()== AttackResult.ResultType.BLOCKED&&event.isParried()) {
                if(event.getDamageSource().getEntity() instanceof LivingEntity livingEntity) {
                    LivingEntityPatch<?> livingEntityPatch = EpicFightCapabilities.getEntityPatch(livingEntity, LivingEntityPatch.class);
                    if (livingEntityPatch instanceof IAdvancedCapability attackerPatch) {
                        if(event.getDamageSource().is(AnimationEffectManager.BYPASS_GUARD_ONLY)){
                            attackerPatch.setStamina(Math.max(0, (float) (attackerPatch.getStamina() - attackerPatch.getMaxStamina() * 0.04 +1.5)));
                            if(livingEntity.hasEffect(StarsEffect.INSTABILITY.get())) {
                                int level = livingEntity.getEffect(StarsEffect.INSTABILITY.get()).getAmplifier();
                                if(level>=8&&level<12){
                                    Player player=container.getExecutor().getOriginal();
                                    level%=4;
                                    String string="";
                                    if(level==0){
                                        string="venom";
                                    }
                                    if(level==1){
                                        string="flame";
                                    }
                                    if(level==2){
                                        string="freeze";
                                    }
                                    if(level==3){
                                        string="spark";
                                    }

                                    DOTEPassive.setWeaponImbuement(player.level(), player.getMainHandItem(), string, 300);
                                    livingEntity.removeEffect(StarsEffect.INSTABILITY.get());
                                }
                            }
                        }

                    }

                }
            }

        });
    }


    public static class Builder extends SkillBuilder<SeeThrough2> {
    }
}