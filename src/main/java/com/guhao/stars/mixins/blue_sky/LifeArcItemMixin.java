package com.guhao.stars.mixins.blue_sky;

import com.legacy.blue_skies.items.arcs.ArcItem;
import com.legacy.blue_skies.items.arcs.LifeArcItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = LifeArcItem.class,remap = false)
public class LifeArcItemMixin extends ArcItem {
    public LifeArcItemMixin(int slotId) {
        super(slotId);
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onHit(ItemStack stack, Player player, LivingDamageEvent event) {
        int level = this.getFunctionalLevel(stack, player);
        float reductionHealth = level == 0 ? 10.0F : (level == 1 ? 20.0F : 30.0F);
        if (player.getRemainingFireTicks() > 0 && player.getHealth() <= reductionHealth) {
            event.setAmount(event.getAmount() / 2.0F);
        }
    }
}
