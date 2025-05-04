package com.guhao.stars.mixins.blue_sky;

import com.legacy.blue_skies.items.arcs.ArcItem;
import com.legacy.blue_skies.items.arcs.PoisonArcItem;
import com.legacy.blue_skies.registries.SkiesEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(value = PoisonArcItem.class,remap = false)
public class PoisonArcItemMixin extends ArcItem {

    public PoisonArcItemMixin(int slotId) {
        super(slotId);
    }
    @Unique
    private static final AttributeModifier[] STAR_DAMAGE_BOOSTS;
    static {
        STAR_DAMAGE_BOOSTS = new AttributeModifier[]{new AttributeModifier(UUID.fromString("f785f3aa-eea3-42e4-97a0-5271490e0b94"), "poison_damage_1", 0.5, AttributeModifier.Operation.ADDITION), new AttributeModifier(UUID.fromString("5752252b-ed14-4e03-b149-7e05c43152ed"), "poison_damage_2", 1.0, AttributeModifier.Operation.ADDITION), new AttributeModifier(UUID.fromString("f24a19fe-2893-4372-a5b3-11b5d571a7d7"), "poison_damage_3", 1.5, AttributeModifier.Operation.ADDITION), new AttributeModifier(UUID.fromString("948033ce-317c-4d6a-bb14-1ef306ebe88e"), "poison_damage_4", 2.5, AttributeModifier.Operation.ADDITION)};
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onUnequip(ItemStack stack, Player player) {
        AttributeInstance attribute = player.getAttribute(Attributes.ATTACK_DAMAGE);

        for (AttributeModifier starDamageBoost : STAR_DAMAGE_BOOSTS) {
            if (attribute != null && attribute.hasModifier(starDamageBoost)) {
                attribute.removeModifier(starDamageBoost);
            }
        }

    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void serverTick(ItemStack stack, ServerPlayer player) {
        AttributeInstance attribute = player.getAttribute(Attributes.ATTACK_DAMAGE);
        boolean isPoisoned = player.getEffect(SkiesEffects.DEADLY_VENOM) != null || player.getEffect(MobEffects.POISON) != null;
        int level = this.getFunctionalLevel(stack, player);
        if (isPoisoned) {
            if (attribute != null && !attribute.hasModifier(STAR_DAMAGE_BOOSTS[level])) {
                attribute.addTransientModifier(STAR_DAMAGE_BOOSTS[level]);
            }
        } else {
            for (AttributeModifier starDamageBoost : STAR_DAMAGE_BOOSTS) {
                if (attribute != null && attribute.hasModifier(starDamageBoost)) {
                    attribute.removeModifier(starDamageBoost);
                }
            }
        }

    }

}
