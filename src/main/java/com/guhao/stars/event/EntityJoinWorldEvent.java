package com.guhao.stars.event;

import com.guhao.stars.entity.StarAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;



@Mod.EventBusSubscriber

public class EntityJoinWorldEvent {
    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        execute(event,event.getEntity());
    }

    public static void execute(Entity entity) {
        execute(null,entity);
    }

    private static void execute(@Nullable Event event, Entity entity) {

    }
}


