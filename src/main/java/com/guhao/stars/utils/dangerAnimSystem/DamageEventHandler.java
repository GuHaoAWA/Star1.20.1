// DamageEventHandler.java
package com.guhao.stars.utils.dangerAnimSystem;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import com.guhao.stars.StarsMod;

@Mod.EventBusSubscriber(modid = StarsMod.MODID)
public class DamageEventHandler {

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getSource() instanceof EpicFightDamageSource damageSource) {
            LivingEntity attacker = damageSource.getEntity() instanceof LivingEntity ?
                    (LivingEntity) damageSource.getEntity() : null;

            if (attacker != null) {
                // 处理动画穿透效果
                AnimationEffectManager.processDamageSource(damageSource);
            }
        }
    }
}
