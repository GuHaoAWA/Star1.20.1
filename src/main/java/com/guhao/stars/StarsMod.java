package com.guhao.stars;

import com.guhao.stars.efmex.StarSkillCategories;
import com.guhao.stars.efmex.StarSkillDataKeys;
import com.guhao.stars.efmex.StarSkillSlots;
import com.guhao.stars.efmex.StarWeaponCapabilityPresets;
import com.guhao.stars.entity.StarAttributes;
import com.guhao.stars.regirster.Effect;
import com.guhao.stars.regirster.Items;
import com.guhao.stars.regirster.ParticleType;
import com.guhao.stars.regirster.Sounds;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillSlot;

import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiConsumer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.AbstractMap;

@Mod(StarsMod.MODID)
public class StarsMod {
    public static final Logger LOGGER = LogManager.getLogger(StarsMod.class);
    public static final String MODID = "star";

    public StarsMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Effect.REGISTRY.register(bus);
        ParticleType.PARTICLES.register(bus);
        Sounds.REGISTRY.register(bus);
        Items.ITEMS.register(bus);
        StarAttributes.ATTRIBUTES.register(bus);
        bus.addListener(StarWeaponCapabilityPresets::register);
        SkillCategory.ENUM_MANAGER.registerEnumCls("star", StarSkillCategories.class);
        SkillSlot.ENUM_MANAGER.registerEnumCls("star", StarSkillSlots.class);
        StarSkillDataKeys.DATA_KEYS.register(bus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }

    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int tick, Runnable action) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
            workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    @SubscribeEvent
    public void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
            workQueue.forEach(work -> {
                work.setValue(work.getValue() - 1);
                if (work.getValue() == 0)
                    actions.add(work);
            });
            actions.forEach(e -> e.getKey().run());
            workQueue.removeAll(actions);
        }
    }
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        // 注册客户端事件


    }
}
