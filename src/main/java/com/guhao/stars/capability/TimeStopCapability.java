package com.guhao.stars.capability;

import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

public class TimeStopCapability extends WeaponCapability {
    public TimeStopCapability(CapabilityItem.Builder builder) {
        super(builder);
    }

    public boolean checkOffhandValid(LivingEntityPatch<?> entitypatch) {
        ItemStack offhandItme = entitypatch.getOriginal().getOffhandItem();
        CapabilityItem itemCap = EpicFightCapabilities.getItemStackCapability(offhandItme);
        boolean isFist = itemCap.getWeaponCategory() == WeaponCategories.FIST;
        return isFist || !(offhandItme.getItem() instanceof SwordItem) && !(offhandItme.getItem() instanceof DiggerItem);
    }

    public boolean canHoldInOffhandAlone() {
        return true;
    }
}
