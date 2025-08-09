package com.guhao.stars.network.timestop;

import com.guhao.stars.units.StarDataUnit;
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
            // 确保只在客户端处理
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                com.guhao.stars.units.StarDataUnit.handleTimeStopSync(packet.timeStopped);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}