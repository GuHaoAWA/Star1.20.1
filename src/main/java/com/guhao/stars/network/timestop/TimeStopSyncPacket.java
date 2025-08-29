package com.guhao.stars.network.timestop;

import com.dfdyz.epicacg.client.screeneffect.HsvFilterEffect;
import com.dfdyz.epicacg.event.ScreenEffectEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TimeStopSyncPacket {
    private final boolean timeStopped;

    public TimeStopSyncPacket(boolean timeStopped) {
        this.timeStopped = timeStopped;
    }

    public static void encode(TimeStopSyncPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.timeStopped);
    }

    public static TimeStopSyncPacket decode(FriendlyByteBuf buffer) {
        return new TimeStopSyncPacket(buffer.readBoolean());
    }

    public static void handle(TimeStopSyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                com.guhao.stars.units.StarDataUnit.handleTimeStopSync(packet.timeStopped);
                if (packet.timeStopped && Minecraft.getInstance().player != null) {
                    HsvFilterEffect effect = new HsvFilterEffect(Minecraft.getInstance().player.position(),180);
                    ScreenEffectEngine.PushScreenEffect(effect);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}