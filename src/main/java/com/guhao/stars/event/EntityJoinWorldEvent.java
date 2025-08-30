package com.guhao.stars.event;

import com.guhao.stars.StarsMod;
import com.guhao.stars.efmex.StarSkillSlots;
import com.guhao.stars.regirster.StarSkill;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import javax.annotation.Nullable;


@Mod.EventBusSubscriber(modid = StarsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)

public class EntityJoinWorldEvent {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        execute(event,event.getEntity());
    }

    public static void execute(Entity entity) {
        execute(null,entity);
    }

    private static void execute(@Nullable Event event, Entity entity) {
        PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(entity, PlayerPatch.class);
        if (pp == null) return;
        if (!pp.getSkill(StarSkillSlots.DOTE).hasSkill(StarSkill.DOTE)) {
            pp.getSkill(StarSkillSlots.DOTE).setSkill(StarSkill.DOTE);
        }
    }
    /*@SubscribeEvent
    public static void onServerStarting(RegisterCommandsEvent event) {
        TimeStopCommand.register(event.getDispatcher());
    }*/
}


