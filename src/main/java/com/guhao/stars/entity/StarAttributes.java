package com.guhao.stars.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(
        modid = "star",
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class StarAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, "star");;
    public static final RegistryObject<Attribute> PARRY_STAMINA_LOSE = ATTRIBUTES.register("parry_stamina_lose", () -> (new RangedAttribute("attribute.name.star.parry_stamina_lose", 2.5, 0.0, 1024.0)).setSyncable(true));
    public static final RegistryObject<Attribute> HIT_STAMINA_LOSE = ATTRIBUTES.register("hit_stamina_lose", () -> (new RangedAttribute("attribute.name.star.hit_stamina_lose", 0.0, 0.0, 1024.0)).setSyncable(true));;
    public static final RegistryObject<Attribute> SEETHROUGH_REGEN = ATTRIBUTES.register("seethrough_regen", () -> (new RangedAttribute("attribute.name.star.seethrough_regen", 2.0, 0.0, 1024.0)).setSyncable(true));;

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        for (EntityType<? extends LivingEntity> entityType : event.getTypes()) {
            event.add(entityType, PARRY_STAMINA_LOSE.get());
            event.add(entityType, HIT_STAMINA_LOSE.get());
            event.add(entityType, SEETHROUGH_REGEN.get());
        }
    }
}
