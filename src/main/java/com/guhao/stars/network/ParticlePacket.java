package com.guhao.stars.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ParticlePacket {
    private final ParticleOptions particle;
    private final double x, y, z;
    private final double dx, dy, dz;

    public ParticlePacket(ParticleOptions particle, double x, double y, double z, double dx, double dy, double dz) {
        this.particle = particle;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public static void encode(ParticlePacket packet, FriendlyByteBuf buffer) {
        buffer.writeRegistryId(ForgeRegistries.PARTICLE_TYPES, packet.particle.getType());
        packet.particle.writeToNetwork(buffer);
        buffer.writeDouble(packet.x);
        buffer.writeDouble(packet.y);
        buffer.writeDouble(packet.z);
        buffer.writeDouble(packet.dx);
        buffer.writeDouble(packet.dy);
        buffer.writeDouble(packet.dz);
    }

    public static ParticlePacket decode(FriendlyByteBuf buffer) {
        ParticleType<?> type = buffer.readRegistryIdSafe(ParticleType.class);
        ParticleOptions particle = readParticle(buffer, type);
        return new ParticlePacket(
                particle,
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble()
        );
    }

    private static <T extends ParticleOptions> T readParticle(FriendlyByteBuf buf, ParticleType<T> type) {
        return type.getDeserializer().fromNetwork(type, buf);
    }

    public static void handle(ParticlePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // 只在客户端执行
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                level.addParticle(
                        packet.particle,
                        packet.x, packet.y, packet.z,
                        packet.dx, packet.dy, packet.dz
                );
            }
        });
        ctx.get().setPacketHandled(true);
    }
}