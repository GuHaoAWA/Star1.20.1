package com.guhao.stars.efmex.skills;

import com.guhao.stars.efmex.StarAnimations;
import com.guhao.stars.units.StarDataUnit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;


public class SuperPunchPassive extends Skill {
	private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("0885cf99-5c63-4121-b229-e8f18339ef94");
	private static final UUID EVENT_UUID = UUID.fromString("d706b5bc-b98d-cc91-b83e-16ae515db349");
	public SuperPunchPassive(Builder<? extends Skill> builder) {
		super(builder);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.HURT_EVENT_POST, EVENT_UUID, (event) -> {
			ServerPlayerPatch executer = event.getPlayerPatch();
			DynamicAnimation animation = executer.getAnimator().getPlayerFor(null).getAnimation();
			if (animation == StarAnimations.OLA) {
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
	}
	public void onRemoved(SkillContainer container) {
		container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_POST, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID);
	}
}