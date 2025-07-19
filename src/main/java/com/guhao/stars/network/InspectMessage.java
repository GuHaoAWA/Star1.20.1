package com.guhao.stars.network;

import com.guhao.stars.StarsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class InspectMessage {
    int type, pressedms;

    public InspectMessage(int type, int pressedms) {
        this.type = type;
        this.pressedms = pressedms;
    }

    public InspectMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
        this.pressedms = buffer.readInt();
    }

    public static void buffer(InspectMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
        buffer.writeInt(message.pressedms);
    }

    public static void handler(InspectMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            pressAction(context.getSender(), message.type, message.pressedms);
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player entity, int type, int pressedms) {
        Level world = entity.level();
        if (!world.hasChunkAt(entity.blockPosition()))
            return;
        if (type == 0) {
            if (world.isClientSide()) {
                ItemStack itemstack = null;
                Item item = null;
                if (Minecraft.getInstance().player != null) {
                    itemstack = Minecraft.getInstance().player.getMainHandItem().copy();
                    item = Minecraft.getInstance().player.getMainHandItem().getItem();
                }
                boolean i = item instanceof SwordItem;
                boolean k = item instanceof BowItem;
                boolean l = item instanceof ProjectileWeaponItem;
                boolean d = item instanceof ShieldItem;
                boolean s = item instanceof TridentItem;
                boolean a = item instanceof AxeItem;
                if (itemstack != null && (i || k || l || d || s || a)) {
                    Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
                }
            }


        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        StarsMod.addNetworkMessage(InspectMessage.class, InspectMessage::buffer, InspectMessage::new, InspectMessage::handler);
    }
}
