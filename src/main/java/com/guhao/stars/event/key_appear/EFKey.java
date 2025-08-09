package com.guhao.stars.event.key_appear;

import com.guhao.init.Items;
import com.guhao.stars.regirster.StarSkill;
import com.mafuyu404.smartkeyprompts.util.KeyUtils;
import com.mafuyu404.smartkeyprompts.util.PromptUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.LongHitAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.guhao.stars.StarsMod.MODID;
import static com.mafuyu404.smartkeyprompts.util.PromptUtils.addDesc;


@Mod.EventBusSubscriber(modid= MODID, value= Dist.CLIENT)
public class EFKey {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = Minecraft.getInstance().player;
        PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
        if (pp == null || !pp.isBattleMode() || Minecraft.getInstance().screen != null) return;

        PromptUtils.show("epicfight", "key.epicfight.switch_mode");
        PromptUtils.show("epicfight", "key.epicfight.lock_on");
        {
            Vec3 playerPosition = new Vec3(player.getX(), player.getEyeY(), player.getZ());
            getNearbyEntities(player.level, playerPosition).forEach((targetEntity) -> {
                LivingEntityPatch<?> targetPatch = (LivingEntityPatch<?>)EpicFightCapabilities.getEntityPatch(targetEntity, LivingEntityPatch.class);
                if (targetPatch != null && targetEntity != player) {

                    if (isAnimationValid(targetPatch)) {
                        addDesc("§4处决").forKey(KeyUtils.getKeyByDesc("key.use")).withCustom(true).toGroup("minecraft");
                    }
                }

            });
        }
        if (player.getMainHandItem().is(Items.GUHAO.get())) return;
        PromptUtils.show("epicfight", "key.epicfight.attack");

        if (pp.getSkill(SkillSlots.WEAPON_INNATE) != null && pp.getEntityState().canUseSkill() && pp.getSkill(SkillSlots.WEAPON_INNATE).getStack() > 0) {
            int stack = pp.getSkill(SkillSlots.WEAPON_INNATE).getStack();
            if (pp.getSkill(StarSkill.THE_WORLD) != null && stack >= 3) {
                addDesc("§e凝结时间").withKeyAlias("按住shift+" + KeyUtils.getKeyDisplayName("key.epicfight.weapon_innate_skill")).forKey(KeyUtils.getKeyByDesc("key.epicfight.weapon_innate_skill")).withCustom(true).toGroup("epicfight");
            }
            PromptUtils.show("epicfight", "key.epicfight.weapon_innate_skill");
        }

    }
    private static List<LivingEntity> getNearbyEntities(Level level, Vec3 playerPosition) {
        return level.getEntitiesOfClass(LivingEntity.class, (new AABB(playerPosition, playerPosition)).inflate(3.0), (e) -> true).stream().sorted(Comparator.comparingDouble((entity) -> entity.distanceToSqr(playerPosition))).limit(2L).toList();
    }
    private static boolean isAnimationValid(LivingEntityPatch<?> targetPatch) {
        label26: {
            DynamicAnimation var4 = targetPatch.getAnimator().getPlayerFor(null).getAnimation();
            if (var4 instanceof StaticAnimation staticAnimation) {
                if (staticAnimation == Animations.BIPED_KNEEL) {
                    break label26;
                }
            }

            var4 = targetPatch.getAnimator().getPlayerFor(null).getAnimation();
            if (var4 instanceof LongHitAnimation longHitAnimation) {
                if (Set.of(Animations.WITHER_NEUTRALIZED, Animations.VEX_NEUTRALIZED, Animations.SPIDER_NEUTRALIZED, Animations.DRAGON_NEUTRALIZED, Animations.ENDERMAN_NEUTRALIZED, Animations.BIPED_COMMON_NEUTRALIZED, Animations.GREATSWORD_GUARD_BREAK).contains(longHitAnimation)) {
                    break label26;
                }
            }
            return false;
        }
        return true;
    }
}
