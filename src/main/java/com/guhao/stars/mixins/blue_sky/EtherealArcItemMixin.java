package com.guhao.stars.mixins.blue_sky;

import com.legacy.blue_skies.items.arcs.ArcItem;
import com.legacy.blue_skies.items.arcs.EtherealArcItem;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(value = EtherealArcItem.class,remap = false)
public abstract class EtherealArcItemMixin extends ArcItem {
    @Unique
    private static final AttributeModifier[] STAR_SPEED_BOOSTS;

    static {
        STAR_SPEED_BOOSTS = new AttributeModifier[]{new AttributeModifier(UUID.fromString("9b997a54-73f4-4cea-9527-d24220cc98f6"), "ethereal_speed_1", 0.01, AttributeModifier.Operation.MULTIPLY_TOTAL), new AttributeModifier(UUID.fromString("33b4f594-371b-11e9-b210-d663bd873d94"), "ethereal_speed_2", 0.02, AttributeModifier.Operation.MULTIPLY_TOTAL), new AttributeModifier(UUID.fromString("945684da-5657-4294-9342-803c3528dbc6"), "ethereal_speed_3", 0.04, AttributeModifier.Operation.MULTIPLY_TOTAL), new AttributeModifier(UUID.fromString("9f6ef2b9-a342-4a6f-adf9-13b30fb71ea7"), "ethereal_speed_4", 0.06, AttributeModifier.Operation.MULTIPLY_TOTAL)};
    }

    public EtherealArcItemMixin(int slotId) {
        super(slotId);
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onEquip(ItemStack stack, Player player) {
        AttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        int level = this.getFunctionalLevel(stack, player);
        if (attribute != null && !attribute.hasModifier(STAR_SPEED_BOOSTS[level])) {
            attribute.addTransientModifier(STAR_SPEED_BOOSTS[level]);
        }

    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onUnequip(ItemStack stack, Player player) {
        AttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);

        for (AttributeModifier starSpeedBoost : STAR_SPEED_BOOSTS) {
            if (attribute != null && attribute.hasModifier(starSpeedBoost)) {
                attribute.removeModifier(starSpeedBoost);
            }
        }
    }
}
