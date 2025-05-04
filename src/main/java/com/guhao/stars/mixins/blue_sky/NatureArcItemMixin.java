package com.guhao.stars.mixins.blue_sky;

import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.items.arcs.ArcItem;
import com.legacy.blue_skies.items.arcs.NatureArcItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = NatureArcItem.class,remap = false)
public abstract class NatureArcItemMixin extends ArcItem {

    public NatureArcItemMixin(int slotId) {
        super(slotId);
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void serverTick(ItemStack stack, ServerPlayer player) {
        SkiesPlayer.ifPresent(player, (skiesPlayer) -> {
            int regenRate = 100;
            if (player.tickCount % regenRate == 0 && player.getLastDamageSource() == null) {
                float maxHealth = (float)((this.getFunctionalLevel(stack, player)) * 2);
                if (this.getFunctionalLevel(stack, player) == 0) {
                    maxHealth = 0.5f;
                }
                float currentHealth = skiesPlayer.getNatureHealth();
                if (currentHealth < maxHealth) {
                    skiesPlayer.setNatureHealth(Math.min(currentHealth + 1.0F, maxHealth));
                }
            }
        });
    }
}
