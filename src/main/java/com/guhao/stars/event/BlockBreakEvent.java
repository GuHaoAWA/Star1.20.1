package com.guhao.stars.event;

import com.guhao.stars.StarsMod;
import com.guhao.stars.entity.StarAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = StarsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlockBreakEvent {

    @SubscribeEvent
    public static void onLootDrop(LivingDropsEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            double lootBoost = player.getAttributeValue(StarAttributes.LOOTING.get());

            if (lootBoost > 0) {
                for (ItemEntity drop : event.getDrops()) {
                    ItemStack stack = drop.getItem();
                    int originalCount = stack.getCount();
                    int extraCount = (int) Math.round(originalCount * lootBoost * 0.1);

                    if (extraCount > 0) {
                        stack.setCount(originalCount + extraCount);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        BlockState state = event.getState();
        Level level = event.getPlayer().level;
        BlockPos pos = event.getPos();

        if (player != null && isOreBlock(state.getBlock())) {
            // 每10点属性=1级时运效果
            int fortuneLevel = (int) (player.getAttributeValue(StarAttributes.FORTUNE.get()) / 10);

            if (fortuneLevel > 0) {
                event.setCanceled(true);
                handleOreDrops(player, state, level, pos, fortuneLevel);
            }
        }
    }

    private static boolean isOreBlock(Block block) {
        return block.defaultBlockState().is(BlockTags.MINEABLE_WITH_PICKAXE) &&
                !block.defaultBlockState().is(BlockTags.DIRT);
    }

    private static void handleOreDrops(Player player, BlockState state, Level level, BlockPos pos, int fortuneLevel) {
        List<ItemStack> originalDrops = Block.getDrops(
                state,
                (ServerLevel) level,
                pos,
                level.getBlockEntity(pos),
                player,
                player.getMainHandItem()
        );

        List<ItemStack> modifiedDrops = applyFortune(originalDrops, fortuneLevel, level.random);

        modifiedDrops.forEach(stack -> {
            if (!stack.isEmpty()) {
                Block.popResource(level, pos, stack);
            }
        });

        level.destroyBlock(pos, false);
    }

    private static List<ItemStack> applyFortune(List<ItemStack> original, int fortune, RandomSource random) {
        List<ItemStack> result = new ArrayList<>();

        for (ItemStack stack : original) {
            if (stack.isEmpty()) continue;

            ItemStack newStack = stack.copy();
            if (shouldApplyFortune(stack.getItem())) {
                int bonus = random.nextInt(fortune + 2) - 1;
                newStack.setCount(stack.getCount() * (Math.max(bonus, 0) + 1));
            }
            result.add(newStack);
        }

        return result;
    }

    private static boolean shouldApplyFortune(Item item) {
        return true;
    }
}