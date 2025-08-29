package com.guhao.stars.efmex.skills;

import com.dfdyz.epicacg.client.screeneffect.HsvFilterEffect;
import com.dfdyz.epicacg.event.ScreenEffectEngine;
import com.guhao.init.ParticleType;
import com.guhao.stars.efmex.StarAnimations;
import com.guhao.stars.regirster.Sounds;
import com.guhao.stars.units.ShakeUnit;
import com.guhao.stars.units.StarDataUnit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;

import static com.guhao.stars.efmex.StarSkillDataKeys.*;
import static com.guhao.stars.efmex.StarSkillDataKeys.CHECK1;


public class TimeStopPassive extends Skill {
	private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("0885cf99-5c63-4121-b229-e8f18339ef94");
	private static final UUID EVENT_UUID = UUID.fromString("d706b5bc-b98b-cc91-b83e-16ae595db349");
	public TimeStopPassive(Builder<? extends Skill> builder) {
		super(builder);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.HURT_EVENT_POST, EVENT_UUID, (event) -> {
			ServerPlayerPatch executer = event.getPlayerPatch();
			DynamicAnimation animation = executer.getAnimator().getPlayerFor(null).getAnimation();
			if (animation == StarAnimations.THE_WORLD || animation == StarAnimations.OLA) {
				event.getDamageSource().setStunType(StunType.NONE);
			}
		});
		container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID, (event) -> {
			if (container.getExecuter().getAnimator().getPlayerFor(null).getAnimation() instanceof StaticAnimation staticAnimation && staticAnimation == StarAnimations.OLA) {
				LocalPlayer clientPlayer = event.getPlayerPatch().getOriginal();
				clientPlayer.setSprinting(false);
				clientPlayer.sprintTriggerTime = -1;
				Minecraft mc = Minecraft.getInstance();
				ControllEngine.setKeyBind(mc.options.keySprint, false);
			}

		});

		container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID, (event) -> {
			if (event.getResult() == AttackResult.ResultType.SUCCESS) return;
			if (container.getDataManager().getDataValue(TIME_TICK.get()) >= 899 && event.getAmount() >= event.getPlayerPatch().getOriginal().getHealth()) {
				container.getDataManager().setDataSync(TIME_TICK.get(),0,event.getPlayerPatch().getOriginal());
				event.setResult(AttackResult.ResultType.MISSED);
				event.setParried(false);
				event.setAmount(0);
				if (event.getPlayerPatch().getOriginal().level() instanceof ServerLevel _level) {
					_level.sendParticles(ParticleType.Y_CONQUEROR_HAKI_FLOOR.get(), event.getPlayerPatch().getOriginal().getX(), event.getPlayerPatch().getOriginal().getY() + 1.0, event.getPlayerPatch().getOriginal().getZ(), 1, 0.1, 0.1, 0.1, 0);
					_level.sendParticles(ParticleType.Y_CONQUEROR_HAKI.get(), event.getPlayerPatch().getOriginal().getX(), event.getPlayerPatch().getOriginal().getY() + 1.0, event.getPlayerPatch().getOriginal().getZ(), 1, 0.1, 0.1, 0.1, 0);
				}
				event.getPlayerPatch().playSound(Sounds.WRYYYYY.get(), 1.0F,1.0F,1.0F);
				HsvFilterEffect screen_effect = new HsvFilterEffect(event.getPlayerPatch().getOriginal().position(),14);
				ScreenEffectEngine.PushScreenEffect(screen_effect);
				ShakeUnit.shake(40,10,10,event.getPlayerPatch().getOriginal().position,10);

				event.getPlayerPatch().playSound(Sounds.SUPER_TIME.get(),1.0f,1.0f);
				event.getPlayerPatch().getOriginal().setHealth(event.getPlayerPatch().getOriginal().getMaxHealth());
				event.getPlayerPatch().getOriginal().getFoodData().setFoodLevel(20);
				event.getPlayerPatch().getOriginal().getFoodData().setSaturation(20);
				event.getPlayerPatch().getOriginal().getActiveEffects().removeIf(
						effect -> !effect.getEffect().isBeneficial()
				);
				event.setCanceled(true);
			}
		});

	}
	@Override
	public void updateContainer(SkillContainer container) {
		super.updateContainer(container);
		AttributeInstance speed = container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED);
		if (speed != null && container.getExecuter().getAnimator().getPlayerFor(null).getAnimation().getRegistryName() == StarAnimations.OLA.getRegistryName()) {
			speed.removeModifier(SPEED_MODIFIER_UUID);
			AttributeModifier modifier = new AttributeModifier(
					SPEED_MODIFIER_UUID,
					"speed_slow",
					container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).getValue() * -0.85f,
					AttributeModifier.Operation.ADDITION
			);
			speed.addTransientModifier(modifier);
		}
		if (container.getExecuter().getOriginal() instanceof ServerPlayer serverPlayer) {
			if (container.getDataManager().getDataValue(TIME_TICK.get()) <= 900) container.getDataManager().setDataSync(TIME_TICK.get(), container.getDataManager().getDataValue(TIME_TICK.get()) + 1 , serverPlayer);
		}
		if (StarDataUnit.isTimeStopped()) {
			if (!container.getExecuter().isLogicalClient()) {
				Skill skill = container.getExecuter().getSkill(SkillSlots.WEAPON_INNATE).getSkill();
				skill.setStackSynchronize((ServerPlayerPatch) container.getExecuter(), 1);
			}
		}
	}
	public void onRemoved(SkillContainer container) {
		container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_POST, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID);
	}
}