package com.guhao.stars.client;


import com.guhao.stars.client.model.CosmicModelLoader;
import com.guhao.stars.client.post.StarShaders;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.guhao.stars.StarsMod.MODID;


@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class StarClient {

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) {
        StarShaders.registerCosmicShaders(event);
        StarShaders.registerStarrySkyShaders(event);
        StarShaders.registerStarrySkyItemShaders(event);
    }

    @SubscribeEvent
    public static void registerLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register("cosmic", CosmicModelLoader.INSTANCE);

    }

}