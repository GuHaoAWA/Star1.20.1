package com.guhao.stars.event;

import com.guhao.stars.regirster.Sounds;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Objects;

@Mod.EventBusSubscriber
public class CialloEvent {
    @SubscribeEvent
    public static void onPlayerTick(ServerChatEvent event) {
        execute(event, event.getPlayer().level(), event.getMessage().getString(), event.getPlayer());
    }

    public static void execute(Level world, String message, Player player) {
        execute(null, world, message, player);
    }

    private static void execute(@Nullable Event event, Level world, String message, Player player) {
        if (message.equals("ciallo") || message.equals("Ciallo")) {
            if (!world.isClientSide() && player.getServer() != null) {
                Objects.requireNonNull(player.level().getServer()).getPlayerList().broadcastSystemMessage(Component.literal("§dCiallo～(∠・ω< )⌒☆"),false);
                for (ServerPlayer serverPlayer : player.level().getServer().getPlayerList().getPlayers()) {
                    ServerPlayerPatch pp = EpicFightCapabilities.getEntityPatch(serverPlayer, ServerPlayerPatch.class);
                    pp.playSound(Sounds.CAILLO.get(),1.0f,1.0f,1.0f);
                }
            }
        }
    }
}
