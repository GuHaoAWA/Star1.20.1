package com.guhao.stars.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class BloodBattle extends Item {
    public BloodBattle(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean isDamageable(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean canBeDepleted() {
        return false;
    }

    @Override
    public boolean isFireResistant() {
        return true;
    }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.literal("§b在背包时开启血战模式，仅为无伤流玩家设计"));
        list.add(Component.literal("§c被打到后立刻重置BOSS"));
        list.add(Component.literal("§1凝聚群星战意......"));
    }
}
