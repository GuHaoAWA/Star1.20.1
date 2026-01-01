package com.guhao.stars.mixins.epicfight;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

// TODO 修改武器附魔击退后的冲击显示问题，改为显示+百分之九
@Mixin(value = CapabilityItem.class, remap = false)
public abstract class CapabilityItemMixin {
    @ModifyConstant(
            method = "modifyItemTooltip",
            constant = @Constant(floatValue = 0.12F),
            remap = false
    )
    private float modifyKnockbackMultiplier(float original) {
        return 0.09F;
    }
}