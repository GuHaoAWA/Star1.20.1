package com.guhao.stars.mixins.blue_sky;

import com.legacy.blue_skies.items.arcs.AquaticArcItem;
import com.legacy.blue_skies.items.arcs.ArcItem;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(value = AquaticArcItem.class,remap = false)
public class AquaticArcItemMixin extends ArcItem {
    public AquaticArcItemMixin(int slotId) {
        super(slotId);
    }
    @Unique
    private static final AttributeModifier[] STAR_SWIM_SPEED_BOOSTS;
    static {
        STAR_SWIM_SPEED_BOOSTS = new AttributeModifier[]{new AttributeModifier(UUID.fromString("f8edab1e-d58d-45b3-9615-d9f72f21c763"), "aquatic_swim_speed_1", 0.04, AttributeModifier.Operation.MULTIPLY_TOTAL), new AttributeModifier(UUID.fromString("4291562c-d877-4c00-b354-92758e421ee0"), "aquatic_swim_speed_2", 0.08, AttributeModifier.Operation.MULTIPLY_TOTAL), new AttributeModifier(UUID.fromString("4291562c-d877-4c00-b354-92758e421ee0"), "aquatic_swim_speed_3", 0.12, AttributeModifier.Operation.MULTIPLY_TOTAL), new AttributeModifier(UUID.fromString("4291562c-d877-4c00-b354-92758e421ee0"), "aquatic_swim_speed_4", 0.15, AttributeModifier.Operation.MULTIPLY_TOTAL)};
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onEquip(ItemStack stack, Player player) {
        AttributeInstance swimSpeed = player.getAttribute(ForgeMod.SWIM_SPEED.get());
        int level = this.getFunctionalLevel(stack, player);
        if (swimSpeed != null && !swimSpeed.hasModifier(STAR_SWIM_SPEED_BOOSTS[Math.min(1, level)])) {
            swimSpeed.addTransientModifier(STAR_SWIM_SPEED_BOOSTS[Math.min(1, level)]);
        }

    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onUnequip(ItemStack stack, Player player) {
        AttributeInstance swimSpeed = player.getAttribute(ForgeMod.SWIM_SPEED.get());
        for (AttributeModifier starSwimSpeedBoost : STAR_SWIM_SPEED_BOOSTS) {
            if (swimSpeed != null && swimSpeed.hasModifier(starSwimSpeedBoost)) {
                swimSpeed.removeModifier(starSwimSpeedBoost);
            }
        }
    }
}
