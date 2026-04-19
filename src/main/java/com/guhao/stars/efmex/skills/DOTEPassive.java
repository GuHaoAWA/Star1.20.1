package com.guhao.stars.efmex.skills;

import com.guhao.stars.efmex.StarAnimations;
import com.guhao.stars.efmex.StarSkillDataKeys;
import com.guhao.stars.entity.StarAttributes;
import com.guhao.stars.regirster.StarSkill;
import com.guhao.stars.regirster.StarsEffect;
import com.guhao.stars.regirster.StarsSounds;
import com.guhao.stars.utils.dangerAnimSystem.AnimationEffectManager;
import com.hm.efn.gameasset.animations.EFNSkillAnimations;
import com.hm.efn.particle.EFNParticles;
import com.nameless.impactful.network.CPApplyShake;
import com.nameless.impactful.network.NetWorkManger;
import com.nameless.indestructible.world.capability.AdvancedCustomMobPatch;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.effect.EpicFightMobEffects;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.List;
import java.util.UUID;




@SuppressWarnings("removal")
public class DOTEPassive extends Skill {
    private static final UUID EVENT_UUID = UUID.fromString("071dda48-0cdd-4c92-9787-c0efb1524e8b");
    private static final UUID DAMAGE_EVENT_UUID = UUID.fromString("071dda48-0cdd-4c92-9787-c1efb1524e8b");

    public DOTEPassive(DOTEPassive.Builder builder) {
        super(builder);
    }

    public static DOTEPassive.Builder createDOTEPassiveBuilder() {
        return (new DOTEPassive.Builder());
    }

    public static void breakdown(SkillContainer container) {
        container.getExecutor().getOriginal().addEffect(new MobEffectInstance(EpicFightMobEffects.STUN_IMMUNITY.get(), 30, 30));
        container.getExecutor().setStamina(container.getExecutor().getMaxStamina());
        SkillContainer targetContainer = container.getExecutor().getSkill(StarSkill.DOTE);
        if (container.getExecutor().getOriginal() instanceof ServerPlayer serverPlayer) {
            targetContainer.getDataManager().setDataSync(StarSkillDataKeys.WEAKNESS_COUNT_2.get(), 30.0f, serverPlayer);
        }
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);



        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_ATTACK, EVENT_UUID, (e) -> {
            EpicFightDamageSource efd = AnimationEffectManager.getEpicFightDamageSources(e.getDamageSource());
            float impact = 0.0f;
            if (efd != null) impact = efd.getBaseImpact();
            if (container.getExecutor().getStamina() <= container.getExecutor().getMaxStamina() * 0.25f) {
                float reduce_stamina = Math.min(Math.max(e.getDamage() * 0.1f, impact * 0.2f), 1.5f);
                container.getExecutor().setStamina(container.getExecutor().getStamina() - reduce_stamina);
            }
        }, 999);
        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_DAMAGE, EVENT_UUID, (e) -> {
            EpicFightDamageSource efd = AnimationEffectManager.getEpicFightDamageSources(e.getDamageSource());
            float impact = 0.0f;
            if (efd != null) impact = efd.getBaseImpact();
            if (container.getExecutor().getStamina() <= container.getExecutor().getMaxStamina() * 0.25f) {
                float reduce_stamina = Math.min(Math.max(e.getDamage() * 0.1f, impact * 0.2f), 1.5f);
                if (reduce_stamina > container.getExecutor().getStamina()) {
                    container.getExecutor().playAnimationSynchronized(Animations.BIPED_COMMON_NEUTRALIZED, 0.0f);
                    container.getExecutor().applyStun(StunType.NEUTRALIZE, 5.0f);
                    container.getExecutor().playSound(EpicFightSounds.NEUTRALIZE_MOBS.get(), 1.2f, 1.0f, 1.0f);
                    breakdown(container);
                }
            }
        }, 999);


        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID, (event) -> {
            PlayerPatch<?> playerPatch = container.getExecutor();
            float maxStamina = playerPatch.getMaxStamina();
            playerPatch.setStamina(playerPatch.getStamina() +0.05F*maxStamina);
        });
        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID, (e) -> {
            if (container.getDataManager().getDataValue(StarSkillDataKeys.WEAKNESS_COUNT_2.get()) > 0f) {
                e.setCanceled(true);
            }
        }, 999);
//        ATTACK_PHASE_END_EVENT
        //对撞
        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_ATTACK, EVENT_UUID, (event) -> {
            if(event.getTarget()!=null) {
                LivingEntity livingEntity=event.getTarget();
                //有失稳buff
                if (livingEntity.hasEffect(StarsEffect.INSTABILITY.get())) {
                    EntityPatch<?> entityPatch = EpicFightCapabilities.getEntityPatch(livingEntity, EntityPatch.class);
                    if (entityPatch != null && entityPatch instanceof LivingEntityPatch<?> livingEntityPatch) {
//                        System.out.println("4444444444    "+ livingEntityPatch.getEntityState().getLevel());
                        int phaseLevelLiving = livingEntityPatch.getEntityState().getLevel();
                        if(phaseLevelLiving<=3&&phaseLevelLiving>0){
                            event.getPlayerPatch().playSound(StarsSounds.BIGBONG.get(), -0.05F, 0.1F);
                            if(container.getExecutor().getOriginal() instanceof ServerPlayer serverPlayer){
                                spawnParryFlashParticle(serverPlayer,livingEntity);
                            }

                            Player player = container.getExecutor().getOriginal();
                            if(hasImbuement(player.getMainHandItem())){
                                clearImbuement(player.getMainHandItem());
                            }
                            int level = livingEntity.getEffect(StarsEffect.INSTABILITY.get()).getAmplifier();
                            if(level>=4){
                                level%=4;
                                String string="";
                                if(level==0)string="venom";
                                if(level==1)string="flame";
                                if(level==2)string="freeze";
                                if(level==3)string="spark";
                                setWeaponImbuement(player.level(), player.getMainHandItem(), string, 300);
                                livingEntityPatch.playAnimationSynchronized(StarAnimations.EFN_GUARD_ACTIVE_HIT3, 0);
                                container.getExecutor().playAnimationSynchronized(StarAnimations.EFN_GUARD_ACTIVE_HIT3, 0);
                            }
                            else {
                                livingEntityPatch.playAnimationSynchronized(StarAnimations.EFN_GUARD_ACTIVE_HIT3, 0);
                                container.getExecutor().playAnimationSynchronized(EFNSkillAnimations.EFN_GUARD_ACTIVE_HIT3, 0);
                                String string="";
                                if(level==0)string="venom";
                                if(level==1)string="flame";
                                if(level==2)string="freeze";
                                if(level==3)string="spark";
                                setWeaponImbuement(player.level(), player.getMainHandItem(), string, 300);
                            }
//                            if(livingEntityPatch instanceof AdvancedCustomMobPatch<?> advancedCustomMobPatch){
//                                executeBossStunEvent(advancedCustomMobPatch, StunType.LONG, 3.0f);
//                            }
                            livingEntity.removeEffect(StarsEffect.INSTABILITY.get());
                        }
                    }
                }
            }

        });




        //震屏
//        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_HURT, EVENT_UUID, (event) -> {
//            if (container.getExecutor().getOriginal() instanceof ServerPlayer serverPlayer) {
//                Impactfulhuh(serverPlayer, 0.6, 6, 0.5, 3);
//            }
//        });

    }

    //震动效果
    public static void Impactfulhuh(ServerPlayer serverPlayer, double intensity, int time, double frequency, int time2) {
        NetWorkManger.sendToPlayer(
                new CPApplyShake(
                        time,           // 持续时间
                        (float) intensity,  // 强度
                        (float) frequency,  // 频率
                        time2           // 衰减时间
                ),
                serverPlayer
        );
    }

    // 清除武器附魔
    public static void clearImbuement(ItemStack weapon) {
        if (weapon == null || weapon.isEmpty()) {
            return;
        }
        weapon.getOrCreateTag().remove("imbueType");
        weapon.getOrCreateTag().remove("imbueExpire");
        weapon.getOrCreateTag().remove("maxImbueTime");
    }
    //设置武器附魔
    public static void setWeaponImbuement(Level world, ItemStack weapon, String imbueType, int durationTicks) {
        if (weapon == null || imbueType == null || imbueType.isEmpty()) {
            return;
        }
        weapon.getOrCreateTag().putString("imbueType", imbueType);
        long expireTime = world.getGameTime() + durationTicks;
        weapon.getOrCreateTag().putLong("imbueExpire", expireTime);
        weapon.getOrCreateTag().putInt("maxImbueTime", durationTicks);
    }

    // 检查武器是否有附魔
    public static boolean hasImbuement(ItemStack weapon) {
        if (weapon == null || weapon.isEmpty()) {
            return false;
        }
        return weapon.getOrCreateTag().contains("imbueType") &&
                !weapon.getOrCreateTag().getString("imbueType").isEmpty();
    }




    public static void executeBossStunEvent(AdvancedCustomMobPatch<?> advancedCustomMobPatch, StunType stunType, float stunTime) {
        advancedCustomMobPatch.applyStun(stunType, stunTime);
    }


    private void spawnParryFlashParticle(ServerPlayer serverPlayer, Entity target) {
        if (target == null) return;
        EFNParticles.EFN_PARRY_FLASH_MAIN.get().spawnParticleWithArgument(
                serverPlayer.serverLevel(),
                (player, entity) -> {
                    Vec3 pos = this.getParticlePositionForAnimation(player, entity);
                    return new Vector3d(pos.x, pos.y, pos.z);
                },
                (player, entity) -> {
                    Vec3 args = this.getParticleArgumentsForAnimation();
                    return new Vector3d(args.x, args.y, args.z);
                },
                serverPlayer,
                target
        );
        EFNParticles.ALL_SPARK.get().spawnParticleWithArgument(
                serverPlayer.serverLevel(),
                (player, entity) -> {
                    Vec3 pos = getParticlePositionForAnimation(player, entity);
                    return new Vector3d(pos.x, pos.y, pos.z);
                },
                HitParticleType.ZERO,
                serverPlayer,
                target
        );
    }


    private Vec3 getParticleArgumentsForAnimation() {
        return new Vec3(1.2F, 0.0F, 0.0F);
    }

    private Vec3 getParticlePositionForAnimation(Entity player, Entity target) {
        Vec3 playerPos = player.position().add(0.0F, player.getBbHeight() * 0.6, 0.0F);
        Vec3 targetPos = target.position().add(0.0F, target.getBbHeight() * 0.6, 0.0F);
        Vec3 middlePos = playerPos.add(targetPos.subtract(playerPos).scale(0.5F));
        return middlePos;
    }

    @Override
    public void onRemoved(SkillContainer container) {

        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_ATTACK, EVENT_UUID);
//        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_ATTACK, DAMAGE_EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_DAMAGE, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_ATTACK, EVENT_UUID);
//        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_HURT, EVENT_UUID);
    }

    @Override
    public void updateContainer(SkillContainer container) {
        if (!container.getDataManager().hasData(StarSkillDataKeys.WEAKNESS_COUNT_2.get())) {
            container.getDataManager().registerData(StarSkillDataKeys.WEAKNESS_COUNT_2.get());
        }

        if (container.getDataManager().getDataValue(StarSkillDataKeys.WEAKNESS_COUNT_2.get()) > 0 && container.getExecutor().getOriginal() instanceof ServerPlayer) {
            container.getDataManager().setDataSync(StarSkillDataKeys.WEAKNESS_COUNT_2.get(), container.getDataManager().getDataValue(StarSkillDataKeys.WEAKNESS_COUNT_2.get()) - 1.0f, (ServerPlayer) container.getExecutor().getOriginal());
        }
    }


    public static class Builder extends SkillBuilder<DOTEPassive> {
    }
}
