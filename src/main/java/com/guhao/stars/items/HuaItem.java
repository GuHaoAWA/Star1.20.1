package com.guhao.stars.items;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class HuaItem extends RecordItem {

    public HuaItem() {
        super(14, () -> ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("star:yuzusoft")), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC), 4760);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack itemstack) {
        return true;
    }

}