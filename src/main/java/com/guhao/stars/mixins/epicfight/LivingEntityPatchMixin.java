package com.guhao.stars.mixins.epicfight;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import yesman.epicfight.world.capabilities.entitypatch.HurtableEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

@Mixin(value = LivingEntityPatch.class,remap = false)
public abstract class LivingEntityPatchMixin<T extends LivingEntity> extends HurtableEntityPatch<T> {
    @Shadow
    public boolean isOffhandItemValid() {
        return true;
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public float getImpact(InteractionHand hand) {
        float impact;
        int i = 0;

        if (hand == InteractionHand.MAIN_HAND) {
            impact = (float)this.original.getAttributeValue(EpicFightAttributes.IMPACT.get());
            i = this.getOriginal().getMainHandItem().getEnchantmentLevel(Enchantments.KNOCKBACK);
        } else {
            if (this.isOffhandItemValid()) {
                impact = (float)this.original.getAttributeValue(EpicFightAttributes.OFFHAND_IMPACT.get());
                i = this.getOriginal().getOffhandItem().getEnchantmentLevel(Enchantments.KNOCKBACK);
            } else {
                impact = (float)this.original.getAttribute(EpicFightAttributes.IMPACT.get()).getBaseValue();
            }
        }

        return impact * (1.0F + i * 0.08F);
    }
}
