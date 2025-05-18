package com.guhao.stars.event;


import com.guhao.stars.efmex.StarSkillSlots;
import com.guhao.stars.entity.StarAttributes;
import com.guhao.stars.regirster.Effect;
import com.guhao.stars.regirster.StarSkill;
import com.guhao.stars.units.StarArrayUnit;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.effect.EpicFightMobEffects;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import javax.annotation.Nullable;
import java.util.UUID;

@Mod.EventBusSubscriber
public class AllEFPlayerTickEvent {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            execute(event,event.player);
        }
    }
    public static void execute(Player player) {
        execute(null,player);
    }
    private static void execute(@Nullable Event event, Player player) {
        PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
        if (pp == null) return;
        if (!pp.getSkill(StarSkillSlots.DOTE).hasSkill(StarSkill.DOTE)) {
            pp.getSkill(StarSkillSlots.DOTE).setSkill(StarSkill.DOTE);
        }
    }
}