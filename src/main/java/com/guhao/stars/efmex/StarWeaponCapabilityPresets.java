package com.guhao.stars.efmex;

import com.guhao.stars.StarsMod;
import com.guhao.stars.regirster.StarSkill;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

import java.util.function.Function;

public class StarWeaponCapabilityPresets {
    public static final Function<Item, CapabilityItem.Builder> SHADOW = (item) ->
            WeaponCapability.builder()
                    .category(WeaponCategories.DAGGER)
                    .styleProvider((playerpatch) -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == WeaponCategories.DAGGER ? Styles.TWO_HAND : Styles.ONE_HAND)
                    .passiveSkill(StarSkill.SHADOW_PASSIVE)
                    .hitSound(EpicFightSounds.BLADE_HIT.get())
                    .swingSound(EpicFightSounds.WHOOSH_SMALL.get())
                    .collider(ColliderPreset.DAGGER)
                    .weaponCombinationPredicator((entitypatch) -> EpicFightCapabilities.getItemStackCapability(entitypatch.getOriginal().getOffhandItem()).getWeaponCategory() == WeaponCategories.DAGGER)
                    .newStyleCombo(Styles.ONE_HAND, Animations.DAGGER_AUTO1, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.DAGGER_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(Styles.TWO_HAND, Animations.DAGGER_DUAL_AUTO1, Animations.DAGGER_DUAL_AUTO2, Animations.DAGGER_DUAL_AUTO3, Animations.DAGGER_DUAL_AUTO4, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
                    .newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
                    .innateSkill(Styles.ONE_HAND, (itemstack) -> EpicFightSkills.EVISCERATE)
                    .innateSkill(Styles.TWO_HAND, (itemstack) -> EpicFightSkills.BLADE_RUSH)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_DUAL_WEAPON)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_DUAL_WEAPON)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_DUAL_WEAPON)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_DUAL_WEAPON)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_DUAL)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_DUAL_WEAPON)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_DUAL_WEAPON)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_DUAL_WEAPON)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_DUAL_WEAPON);
    @SubscribeEvent
    public static void register(WeaponCapabilityPresetRegistryEvent event) {
        Logger LOGGER = LogUtils.getLogger();
        LOGGER.info("Loading WeaponCapability");
        event.getTypeEntry().put(new ResourceLocation(StarsMod.MODID, "shadow"), SHADOW);
        LOGGER.info("WeaponCapability Loaded");
    }
}