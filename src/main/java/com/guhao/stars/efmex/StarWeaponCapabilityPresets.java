package com.guhao.stars.efmex;

import com.guhao.stars.StarsMod;
import com.guhao.stars.capability.TimeStopCapability;
import com.guhao.stars.regirster.ParticleType;
import com.guhao.stars.regirster.Sounds;
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
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

import java.util.function.Function;


@SuppressWarnings("removal")
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


    public static final Function<Item, CapabilityItem.Builder> WUSONG = (item) ->
            WeaponCapability.builder().category(WeaponCategories.TACHI)
                    .styleProvider((entitypatch) -> {
                        /*if (entitypatch instanceof PlayerPatch<?> playerpatch) {
                            if (playerpatch.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().hasData(WUSONG_SHEATH.get()) && playerpatch.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(WUSONG_SHEATH.get())) {
                                return CapabilityItem.Styles.SHEATH;
                            }
                        }*/

                        return CapabilityItem.Styles.TWO_HAND;
                    })
                    .passiveSkill(StarSkill.WUSONG_PASSIVE)
                    .hitSound(EpicFightSounds.BLADE_HIT.get())
                    .collider(ColliderPreset.TACHI)
                    .canBePlacedOffhand(false)
                    .newStyleCombo(Styles.SHEATH,
                            Animations.UCHIGATANA_SHEATHING_AUTO,
                            Animations.UCHIGATANA_SHEATHING_DASH,
                            Animations.UCHIGATANA_SHEATH_AIR_SLASH)
                    .newStyleCombo(Styles.TWO_HAND,
                            Animations.TACHI_AUTO1,
                            Animations.TACHI_AUTO2,
                            Animations.TACHI_AUTO3,
                            Animations.TACHI_DASH,
                            Animations.SWORD_DUAL_AIR_SLASH)
                    .newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
                    .innateSkill(Styles.SHEATH, (itemstack) -> StarSkill.WUSONG_SKILL)
                    .innateSkill(Styles.TWO_HAND, (itemstack) -> StarSkill.WUSONG_SKILL)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(Styles.SHEATH, LivingMotions.IDLE, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
                    .livingMotionModifier(Styles.SHEATH, LivingMotions.KNEEL, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
                    .livingMotionModifier(Styles.SHEATH, LivingMotions.WALK, Animations.BIPED_WALK_UCHIGATANA_SHEATHING)
                    .livingMotionModifier(Styles.SHEATH, LivingMotions.CHASE, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
                    .livingMotionModifier(Styles.SHEATH, LivingMotions.RUN, Animations.BIPED_RUN_UCHIGATANA_SHEATHING)
                    .livingMotionModifier(Styles.SHEATH, LivingMotions.SNEAK, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
                    .livingMotionModifier(Styles.SHEATH, LivingMotions.SWIM, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
                    .livingMotionModifier(Styles.SHEATH, LivingMotions.FLOAT, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
                    .livingMotionModifier(Styles.SHEATH, LivingMotions.FALL, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD);


    public static final Function<Item, CapabilityItem.Builder> THE_WORLD = (item) ->
            WeaponCapability.builder()
                    .newStyleCombo(Styles.ONE_HAND, StarAnimations.FIST_AUTO_1.getAccessor(), StarAnimations.FIST_AUTO_2.getAccessor(), StarAnimations.FIST_AUTO_3.getAccessor(),StarAnimations.FIST_AUTO_4.getAccessor(), Animations.FIST_DASH, Animations.FIST_AIR_SLASH)
                    .innateSkill(Styles.ONE_HAND, (itemstack) -> StarSkill.THE_WORLD)
                    .hitSound(Sounds.PUNCH.get())
                    .passiveSkill(StarSkill.TIME_STOP_PASSIVE)
                    .category(WeaponCategories.FIST)
                    .constructor(TimeStopCapability::new);

    public static final Function<Item, CapabilityItem.Builder> SUPER_PUNCH = (item) ->
            WeaponCapability.builder()
                    .newStyleCombo(Styles.ONE_HAND, StarAnimations.FIST_AUTO_1.getAccessor(), StarAnimations.FIST_AUTO_2.getAccessor(), StarAnimations.FIST_AUTO_3.getAccessor(),StarAnimations.FIST_AUTO_4.getAccessor(), Animations.FIST_DASH, Animations.FIST_AIR_SLASH)
                    .innateSkill(Styles.ONE_HAND, (itemstack) -> StarSkill.SUPER_PUNCH)
                    .hitSound(Sounds.PUNCH.get())
                    .passiveSkill(StarSkill.SUPER_PUNCH_PASSIVE)
                    .category(WeaponCategories.FIST)
                    .hitParticle(ParticleType.AIR_PUNCH_BURST_PARTICLE.get())
                    .constructor(TimeStopCapability::new);
    @SubscribeEvent
    public static void register(WeaponCapabilityPresetRegistryEvent event) {
        Logger LOGGER = LogUtils.getLogger();
        LOGGER.info("Loading WeaponCapability");
        event.getTypeEntry().put(new ResourceLocation(StarsMod.MODID, "shadow"), SHADOW);
        event.getTypeEntry().put(new ResourceLocation(StarsMod.MODID, "wusong"), WUSONG);
        event.getTypeEntry().put(new ResourceLocation(StarsMod.MODID, "the_world"), THE_WORLD);
        event.getTypeEntry().put(new ResourceLocation(StarsMod.MODID, "super_punch"), SUPER_PUNCH);
        LOGGER.info("WeaponCapability Loaded");
    }

}