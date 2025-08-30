package com.guhao.stars.event;

import com.guhao.stars.units.StarDataUnit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EntityJoinTimeWorldEvent {
    /*@SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (!player.level().isClientSide) {
                StarDataUnit.syncTimeStopToPlayer(StarDataUnit.isTimeStopped(), player);
            }
        }
    }*/
}