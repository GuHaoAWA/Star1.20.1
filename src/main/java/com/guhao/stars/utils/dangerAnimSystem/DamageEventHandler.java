package com.guhao.stars.utils.dangerAnimSystem;

import com.guhao.stars.StarsMod;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

@Mod.EventBusSubscriber(modid = StarsMod.MODID)
public class DamageEventHandler {

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getSource() instanceof EpicFightDamageSource damageSource) {
            LivingEntity attacker = damageSource.getEntity() instanceof LivingEntity ?
                    (LivingEntity) damageSource.getEntity() : null;

            if (attacker != null) {
                AnimationEffectManager.processDamageSource(damageSource);
            }
        }
    }
}
