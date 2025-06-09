package com.guhao.stars.efmex.skills;

import com.dfdyz.epicacg.registry.MySkillDataKeys;
import com.guhao.stars.efmex.StarSkillDataKeys;
import com.guhao.stars.entity.StarAttributes;
import com.guhao.stars.regirster.Effect;
import com.guhao.stars.regirster.StarSkill;
import com.guhao.stars.units.StarArrayUnit;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.client.gui.BattleModeGui;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.skill.*;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.effect.EpicFightMobEffects;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.Objects;
import java.util.UUID;

public class DOTEPassive extends Skill {
    private static final UUID EVENT_UUID = UUID.fromString("071dda48-0cdd-4c92-9787-c0efb1524e8b");
    private static final UUID DAMAGE_MODIFIER_UUID = UUID.fromString("4f2c9273-c7d1-46cb-8de8-4571c686c2e4");
    private static final UUID IMPACT_MODIFIER_UUID = UUID.fromString("0385cf99-5c63-4121-b229-e8f13339ef94");
    public DOTEPassive(Builder<? extends Skill> builder) {
        super(builder);
    }
    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);

        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID, (e) -> {
            EpicFightDamageSource efd = StarArrayUnit.getEpicFightDamageSources(e.getDamageSource());
            if (e.getResult() == AttackResult.ResultType.BLOCKED && e.isParried()) {
                if (container.getDataManager().getDataValue(StarSkillDataKeys.WEAKNESS.get()) > 0.0f && container.getExecuter().getOriginal() instanceof ServerPlayer) { container.getDataManager().setDataSync(StarSkillDataKeys.WEAKNESS.get(), container.getDataManager().getDataValue(StarSkillDataKeys.WEAKNESS.get()) - 20.0f, (ServerPlayer) container.getExecuter().getOriginal());}
            }

            float impact = 0.0f;
            if (efd != null) impact = efd.getImpact();
            if (container.getExecuter().getStamina() <= container.getExecuter().getMaxStamina()*0.25f) {
                float reduce_stamina = Math.min(Math.max(e.getAmount()*0.1f,impact*0.2f),1.5f);
                container.getExecuter().setStamina(container.getExecuter().getStamina() - reduce_stamina);
            }
        },999);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.HURT_EVENT_POST, EVENT_UUID, (e) -> {
            EpicFightDamageSource efd = StarArrayUnit.getEpicFightDamageSources(e.getDamageSource());
            float impact = 0.0f;
            if (efd != null) impact = efd.getImpact();
            if (container.getExecuter().getStamina() <= container.getExecuter().getMaxStamina()*0.25f) {
                float reduce_stamina = Math.min(Math.max(e.getAmount()*0.1f,impact*0.2f),1.5f);
                if (reduce_stamina > container.getExecuter().getStamina()) {
                    container.getExecuter().playAnimationSynchronized(Animations.BIPED_COMMON_NEUTRALIZED,0.0f);
                    container.getExecuter().applyStun(StunType.NEUTRALIZE,5.0f);
                    container.getExecuter().playSound(EpicFightSounds.NEUTRALIZE_MOBS.get(), 1.2f,1.0f,1.0f);
                    breakdown(container);
                }
            }
        },999);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID, (e) -> {
            if (container.getDataManager().getDataValue(StarSkillDataKeys.WEAKNESS_COUNT_2.get()) > 0f && container.getExecuter().getOriginal() instanceof ServerPlayer) {
                container.getDataManager().setDataSync(StarSkillDataKeys.WEAKNESS.get(), 600f, (ServerPlayer) container.getExecuter().getOriginal());
                return;
            }
            if (container.getDataManager().getDataValue(StarSkillDataKeys.WEAKNESS.get()) > 0f && container.getExecuter().getOriginal() instanceof ServerPlayer) { container.getDataManager().setDataSync(StarSkillDataKeys.WEAKNESS.get(), container.getDataManager().getDataValue(StarSkillDataKeys.WEAKNESS.get()) - 20.0f, (ServerPlayer) container.getExecuter().getOriginal());}
        },999);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID, (e) -> {
            if (container.getDataManager().getDataValue(StarSkillDataKeys.WEAKNESS_COUNT_2.get()) > 0f) {
                e.setCanceled(true);
            }
        },999);
    }
    @Override
    public void onRemoved(SkillContainer container) {

        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_POST, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID);
    }
    @Override
    public void updateContainer(SkillContainer container) {
        if(!container.getDataManager().hasData(StarSkillDataKeys.WEAKNESS.get())){
            container.getDataManager().registerData(StarSkillDataKeys.WEAKNESS.get());
        }
        if(!container.getDataManager().hasData(StarSkillDataKeys.WEAKNESS_COUNT_2.get())){
            container.getDataManager().registerData(StarSkillDataKeys.WEAKNESS_COUNT_2.get());
        }


        if (container.getDataManager().getDataValue(StarSkillDataKeys.WEAKNESS.get()) > 0 && container.getExecuter().getOriginal() instanceof ServerPlayer) {
            container.getDataManager().setDataSync(StarSkillDataKeys.WEAKNESS.get(), container.getDataManager().getDataValue(StarSkillDataKeys.WEAKNESS.get()) - 1.0f, (ServerPlayer) container.getExecuter().getOriginal());
        }
        if (container.getDataManager().getDataValue(StarSkillDataKeys.WEAKNESS_COUNT_2.get()) > 0 && container.getExecuter().getOriginal() instanceof ServerPlayer) {
            container.getDataManager().setDataSync(StarSkillDataKeys.WEAKNESS_COUNT_2.get(), container.getDataManager().getDataValue(StarSkillDataKeys.WEAKNESS_COUNT_2.get()) - 1.0f, (ServerPlayer) container.getExecuter().getOriginal());
        }
        if (container.getDataManager().getDataValue(StarSkillDataKeys.WEAKNESS.get()) > 0) {
            float remainingTicks = container.getDataManager().getDataValue(StarSkillDataKeys.WEAKNESS.get());
            applyWeaknessEffects(container, remainingTicks);
        } else {
            removeWeaknessEffects(container);
        }
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawOnGui(BattleModeGui gui, SkillContainer container, GuiGraphics guiGraphics, float x, float y) {
        if (container.getDataManager().getDataValue(StarSkillDataKeys.WEAKNESS.get()) > 0f) {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate(0.0F, (float) gui.getSlidingProgression(), 0.0F);
            guiGraphics.blit(new ResourceLocation("star", "textures/gui/skills/weakness.png"), (int) x, (int) y, 24, 24, 0.0F, 0.0F, 1, 1, 1, 1);
            float second = (container.getDataManager().getDataValue(StarSkillDataKeys.WEAKNESS.get())) / 20.0f;
            guiGraphics.drawString(gui.font, String.format("%.1f", second), x+3.0F, y + 6.0F, 16777215, true);
            poseStack.popPose();
        }
    }
    private void applyWeaknessEffects(SkillContainer container, float remainingTicks) {
        float remainingSeconds = remainingTicks / 20f;

        float damageReduction = 0.4f * remainingSeconds;

        AttributeInstance attackDamage = container.getExecuter().getOriginal().getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamage != null) {
            attackDamage.removeModifier(DAMAGE_MODIFIER_UUID);
            AttributeModifier modifier = new AttributeModifier(
                    DAMAGE_MODIFIER_UUID,
                    "weakness_attack_damage",
                    -damageReduction / 100f,
                    AttributeModifier.Operation.MULTIPLY_TOTAL
            );
            attackDamage.addTransientModifier(modifier);
        }

        AttributeInstance impact = container.getExecuter().getOriginal().getAttribute(EpicFightAttributes.IMPACT.get());
        if (impact != null) {
            impact.removeModifier(IMPACT_MODIFIER_UUID);
            AttributeModifier modifier = new AttributeModifier(
                    IMPACT_MODIFIER_UUID,
                    "weakness_impact",
                    -remainingSeconds / 100f,
                    AttributeModifier.Operation.MULTIPLY_TOTAL
            );
            impact.addTransientModifier(modifier);
        }
    }

    private void removeWeaknessEffects(SkillContainer container) {
        AttributeInstance attackDamage = container.getExecuter().getOriginal().getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamage != null) {
            attackDamage.removeModifier(DAMAGE_MODIFIER_UUID);
        }

        AttributeInstance impact = container.getExecuter().getOriginal().getAttribute(EpicFightAttributes.IMPACT.get());
        if (impact != null) {
            impact.removeModifier(IMPACT_MODIFIER_UUID);
        }
    }
    public static void breakdown(SkillContainer container) {
        container.getExecuter().getOriginal().addEffect(new MobEffectInstance(EpicFightMobEffects.STUN_IMMUNITY.get(),30,30));
        container.getExecuter().setStamina(container.getExecuter().getMaxStamina());
        SkillContainer targetContainer = container.getExecuter().getSkill(StarSkill.DOTE);
        if(container.getExecuter().getOriginal() instanceof ServerPlayer serverPlayer) {
            targetContainer.getDataManager().setDataSync(StarSkillDataKeys.WEAKNESS.get(), 1280.0f, serverPlayer);
            targetContainer.getDataManager().setDataSync(StarSkillDataKeys.WEAKNESS_COUNT_2.get(), 30.0f, serverPlayer);
        }
    }
}
