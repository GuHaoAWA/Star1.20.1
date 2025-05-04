package com.guhao.stars.event;


import com.guhao.stars.entity.StarAttributes;
import com.guhao.stars.regirster.Effect;
import com.guhao.stars.units.StarArrayUnit;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.effect.EpicFightMobEffects;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import javax.annotation.Nullable;
import java.util.UUID;

@Mod.EventBusSubscriber
public class AllEFPlayerTickEvent {
    private static final UUID EVENT_UUID = UUID.fromString("071dda48-0cdd-4c92-9787-c0efb1524e8b");

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            execute(event,event.player);
        }
    }
    public static void execute(Player player) {
        execute(null,player);
    }
    private static void execute(@Nullable Event event, Player player) {
        PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
        if (pp == null) return;
        pp.getEventListener().addEventListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID, (e) -> {
            if (e.getResult()== AttackResult.ResultType.BLOCKED && e.isParried()) {
                AdvancedCustomHumanoidMobPatch<?> longpatch = EpicFightCapabilities.getEntityPatch(e.getDamageSource().getDirectEntity(), AdvancedCustomHumanoidMobPatch.class);
                if (longpatch != null) {
                    longpatch.setStamina((float) (longpatch.getStamina() - longpatch.getOriginal().getAttribute(StarAttributes.PARRY_STAMINA_LOSE.get()).getValue()));
                    if (longpatch.getOriginal().hasEffect(Effect.STA.get())) {
                        longpatch.setStamina(longpatch.getStamina() - longpatch.getOriginal().getEffect(Effect.STA.get()).getAmplifier() - 1);
                    }
                }
            }
            EpicFightDamageSource efd = StarArrayUnit.getEpicFightDamageSources(e.getDamageSource());
            float impact = 0.0f;
            if (efd != null) impact = efd.getImpact();
            if (pp.getStamina() <= pp.getMaxStamina()*0.25f) {
                float reduce_stamina = Math.min(Math.max(e.getAmount()*0.1f,impact*0.2f),1.5f);
                pp.setStamina(pp.getStamina() - reduce_stamina);
            }
        },999);
        pp.getEventListener().addEventListener(PlayerEventListener.EventType.HURT_EVENT_POST, EVENT_UUID, (e) -> {
            EpicFightDamageSource efd = StarArrayUnit.getEpicFightDamageSources(e.getDamageSource());
            float impact = 0.0f;
            if (efd != null) impact = efd.getImpact();
            if (pp.getStamina() <= pp.getMaxStamina()*0.25f) {
                float reduce_stamina = Math.min(Math.max(e.getAmount()*0.1f,impact*0.2f),1.5f);
                if (reduce_stamina > pp.getStamina()) {
                    pp.playAnimationSynchronized(Animations.BIPED_COMMON_NEUTRALIZED,0.0f);
                    pp.applyStun(StunType.NEUTRALIZE,5.0f);
                    player.addEffect(new MobEffectInstance(EpicFightMobEffects.STUN_IMMUNITY.get(),30,30));
                    pp.playSound(EpicFightSounds.NEUTRALIZE_MOBS.get(), 1.2f,1.0f,1.0f);
                    pp.setStamina(pp.getMaxStamina());
                }
            }
        },999);
    }
}