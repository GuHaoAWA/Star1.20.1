package com.guhao.stars.efmex;

import com.guhao.stars.StarsMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;

@Mod.EventBusSubscriber(
        modid = StarsMod.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class StarAnimations {
    public StarAnimations() {
    }
    public static StaticAnimation BIPED_PHANTOM_ASCENT_FORWARD_NEW;
    public static StaticAnimation BIPED_PHANTOM_ASCENT_BACKWARD_NEW;
    public static StaticAnimation FIRE_BALL;
    public static StaticAnimation AB_FIRE_BALL;
    public static StaticAnimation SCRATCH;
    public static StaticAnimation KILL;
    public static StaticAnimation KILLED;
    public static StaticAnimation KATANA_FATAL_DRAW_SECOND_NEW;
    public static StaticAnimation EVIL_BLADE;
    public static StaticAnimation HANGDANG;
    public static StaticAnimation AOXUE;
    public static StaticAnimation AOXUE2;
    public static StaticAnimation EXECUTE_WEAPON;
    public static StaticAnimation EXECUTED_WEAPON;

    @SubscribeEvent
    public static void registerAnimations(AnimationRegistryEvent event) {
        event.getRegistryMap().put(StarsMod.MODID, StarAnimations::build);
    }

    private static void build() {
        HumanoidArmature biped = Armatures.BIPED;
    }
}
