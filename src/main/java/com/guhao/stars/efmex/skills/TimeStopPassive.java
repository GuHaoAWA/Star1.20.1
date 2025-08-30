package com.guhao.stars.efmex.skills;

import com.guhao.stars.efmex.StarAnimations;
import com.guhao.stars.regirster.Sounds;
import com.guhao.stars.units.ShakeUnit;
import com.guhao.stars.units.StarDataUnit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.client.events.engine.ControlEngine;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;

import static com.guhao.stars.efmex.StarSkillDataKeys.TIME_TICK;

@SuppressWarnings("removal")
public class TimeStopPassive extends Skill {
	private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("0885cf99-5c63-4121-b229-e8f18339ef94");
	private static final UUID EVENT_UUID = UUID.fromString("d706b5bc-b98b-cc91-b83e-16ae595db349");
	
	public static TimeStopPassive.Builder createTimeStopPassiveBuilder() {
		return (new TimeStopPassive.Builder());
	}

	public static class Builder extends SkillBuilder<TimeStopPassive> {}

	public TimeStopPassive(TimeStopPassive.Builder builder) {
		super(builder);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_ATTACK, EVENT_UUID, (event) -> {
			ServerPlayerPatch executer = event.getPlayerPatch();
			DynamicAnimation animation = executer.getAnimator().getPlayerFor(null).getAnimation().get();
			if (animation == StarAnimations.THE_WORLD || animation == StarAnimations.OLA) {
				if (event.getDamageSource() instanceof EpicFightDamageSource epicSource) {
					epicSource.setStunType(StunType.NONE); // 强制取消所有硬直
				}
			}
		});
		container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID, (event) -> {
			if (container.getExecutor().getAnimator().getPlayerFor(null).getAnimation() instanceof StaticAnimation staticAnimation && staticAnimation == StarAnimations.OLA) {
				LocalPlayer clientPlayer = event.getPlayerPatch().getOriginal();
				clientPlayer.setSprinting(false);
				clientPlayer.sprintTriggerTime = -1;
				Minecraft mc = Minecraft.getInstance();
				ControlEngine.setKeyBind(mc.options.keySprint, false);
			}

		});

		container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_ATTACK, EVENT_UUID, (event) -> {
			if (event.getResult() == AttackResult.ResultType.SUCCESS) return;
			if (container.getDataManager().getDataValue(TIME_TICK.get()) >= 899 && event.getDamage() >= event.getPlayerPatch().getOriginal().getHealth()) {
				container.getDataManager().setDataSync(TIME_TICK.get(),0,event.getPlayerPatch().getOriginal());
				event.setResult(AttackResult.ResultType.MISSED);
				event.setParried(false);
				event.getPlayerPatch().playSound(Sounds.WRYYYYY.get(), 1.0F,1.0F,1.0F);
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
		AttributeInstance speed = container.getExecutor().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED);
		if (speed != null && container.getExecutor().getAnimator().getPlayerFor(null).getAnimation().get().getRegistryName() == StarAnimations.OLA.getRegistryName()) {
			speed.removeModifier(SPEED_MODIFIER_UUID);
			AttributeModifier modifier = new AttributeModifier(
					SPEED_MODIFIER_UUID,
					"speed_slow",
					container.getExecutor().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).getValue() * -0.85f,
					AttributeModifier.Operation.ADDITION
			);
			speed.addTransientModifier(modifier);
		}
		if (container.getExecutor().getOriginal() instanceof ServerPlayer serverPlayer) {
			if (container.getDataManager().getDataValue(TIME_TICK.get()) <= 900) container.getDataManager().setDataSync(TIME_TICK.get(), container.getDataManager().getDataValue(TIME_TICK.get()) + 1 , serverPlayer);
		}
		if (StarDataUnit.isTimeStopped()) {
			if (!container.getExecutor().isLogicalClient()) {
				Skill skill = container.getExecutor().getSkill(SkillSlots.WEAPON_INNATE).getSkill();
				skill.setStackSynchronize(container, 1);
			}
		}
	}
	public void onRemoved(SkillContainer container) {
		container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_DAMAGE, EVENT_UUID);
		container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_ATTACK, EVENT_UUID);
		container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID);
	}
}