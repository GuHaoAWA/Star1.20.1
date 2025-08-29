package com.guhao.stars.entity;

import com.eliotlash.mclib.math.Constant;
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
    public static final RegistryObject<Attribute> HIT_STAMINA_LOSE = ATTRIBUTES.register("hit_stamina_lose", () -> (new RangedAttribute("attribute.name.star.hit_stamina_lose", 0.0, 0.0, 1024.0)).setSyncable(true));
    public static final RegistryObject<Attribute> SEETHROUGH_REGEN = ATTRIBUTES.register("seethrough_regen", () -> (new RangedAttribute("attribute.name.star.seethrough_regen", 2.0, 0.0, 1024.0)).setSyncable(true));
    public static final RegistryObject<Attribute> LOOTING = ATTRIBUTES.register("looting", () -> (new RangedAttribute("attribute.name.star.looting", 1.0, 0.0, Double.MAX_VALUE)).setSyncable(true));
    public static final RegistryObject<Attribute> FORTUNE = ATTRIBUTES.register("fortune", () -> (new RangedAttribute("attribute.name.star.fortune", 1.0, 0.0, Double.MAX_VALUE)).setSyncable(true));
    public static final RegistryObject<Attribute> BLOCK_RATE = ATTRIBUTES.register("block_rate", () -> (new RangedAttribute("attribute.name.star.block_rate", 1.0, 0.0, Double.MAX_VALUE)).setSyncable(true));
    public static final RegistryObject<Attribute> BURDEN = ATTRIBUTES.register("burden", () -> (new RangedAttribute("attribute.name.star.burden", 1.0, 0.0, Double.MAX_VALUE)).setSyncable(true));

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        for (EntityType<? extends LivingEntity> entityType : event.getTypes()) {
            event.add(entityType, PARRY_STAMINA_LOSE.get());
            event.add(entityType, HIT_STAMINA_LOSE.get());
            event.add(entityType, SEETHROUGH_REGEN.get());
            event.add(entityType, LOOTING.get());
            event.add(entityType, FORTUNE.get());
            event.add(entityType, BLOCK_RATE.get());
            event.add(entityType, BURDEN.get());
        }
    }
}
