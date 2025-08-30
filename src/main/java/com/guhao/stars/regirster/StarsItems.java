package com.guhao.stars.regirster;

import com.guhao.stars.items.BloodBattle;
import com.guhao.stars.items.HuaItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.guhao.stars.StarsMod.MODID;

public class StarsItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);


    public static final RegistryObject<Item> HUA = ITEMS.register("hua", HuaItem::new);
    public static final RegistryObject<Item> BLOOD_BATTLE = ITEMS.register("blood_battle", () -> new BloodBattle(new Item.Properties().setNoRepair().rarity(Rarity.EPIC).stacksTo(1)));

}

