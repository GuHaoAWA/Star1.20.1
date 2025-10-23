package com.guhao.stars.event;

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