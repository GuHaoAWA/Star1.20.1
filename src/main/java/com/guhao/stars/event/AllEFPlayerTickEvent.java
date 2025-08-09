package com.guhao.stars.event;


import com.guhao.stars.efmex.StarSkillSlots;
import com.guhao.stars.regirster.StarSkill;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber

public class AllEFPlayerTickEvent {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
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