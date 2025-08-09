package com.guhao.stars.event;


import com.guhao.stars.regirster.Items;
import com.p1nero.dote.entity.custom.boss.DOTEBoss;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber

public class PlayerHurtEvent {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerTick(LivingHurtEvent event) {
        execute(event,event.getEntity(), event.getSource());
        if (event.getEntity() instanceof Player player && event.getSource().getEntity() instanceof DOTEBoss) {
            boolean hasItem = false;
            for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() == Items.BLOOD_BATTLE.get()) {
                    hasItem = true;
                    break;
                }
            }
            if (hasItem) {
                event.setResult(Event.Result.DENY);
            }

        }
    }
    public static void execute(LivingEntity player, DamageSource damageSource) {
        execute(null,player,damageSource);
    }
    private static void execute(@Nullable Event event, LivingEntity livingentity, DamageSource damageSource) {
        if (livingentity instanceof Player player && damageSource.getEntity() instanceof DOTEBoss boss) {
            boolean hasItem = false;
            for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() == Items.BLOOD_BATTLE.get()) {
                    hasItem = true;
                    break;
                }
            }
            if (hasItem) {
                boss.discard();
            }
        }
    }
}