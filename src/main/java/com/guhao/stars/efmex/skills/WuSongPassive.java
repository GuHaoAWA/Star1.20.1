package com.guhao.stars.efmex.skills;

import com.guhao.stars.efmex.StarAnimations;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import yesman.epicfight.api.animation.types.DodgeAnimation;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

import java.util.UUID;

import static com.guhao.stars.efmex.StarSkillDataKeys.*;

@SuppressWarnings("removal")
public class WuSongPassive extends PassiveSkill {

	private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("0385cf99-5c63-4121-b229-e8f18339ef94");
	private static final UUID SPEED2_MODIFIER_UUID = UUID.fromString("0385cf99-5c63-4121-b229-e8f19139ef94");
	private static final UUID EVENT_UUID = UUID.fromString("a416c93a-42cb-11eb-b378-0242ac135992");

	public static WuSongPassive.Builder createWuSongPassiveBuilder() {
		return (new WuSongPassive.Builder());
	}

	public static class Builder extends SkillBuilder<WuSongPassive> {}

	public WuSongPassive(WuSongPassive.Builder builder) {
		super(builder);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.ACTION_EVENT_SERVER, EVENT_UUID, (event) -> {
			if (!(event.getAnimation() instanceof DodgeAnimation)) {
				container.getSkill().setConsumptionSynchronize(event.getPlayerPatch().getSkill(SkillSlots.WEAPON_INNATE), 0.0F);
				container.getSkill().setStackSynchronize(event.getPlayerPatch().getSkill(SkillSlots.WEAPON_INNATE), 0);
			}
		});
		container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID, (event) -> this.onReset(container));
		container.getExecutor().getEventListener().addEventListener(EventType.TAKE_DAMAGE_EVENT_ATTACK, EVENT_UUID, (event) -> {
			if (event.isParried() && event.getResult() == AttackResult.ResultType.BLOCKED) {
				container.getDataManager().setDataSync(CHECK3.get(),20, event.getPlayerPatch().getOriginal());
			}
		});
		container.getExecutor().getEventListener().addEventListener(EventType.DEAL_DAMAGE_EVENT_ATTACK, EVENT_UUID, (event) -> {
			if (event.getDamageSource().getAnimation() == StarAnimations.AOXUE) {
				container.getDataManager().setDataSync(CHECK2.get(),20, event.getPlayerPatch().getOriginal());
			}
			if ((event.getDamageSource().getAnimation() == StarAnimations.AOXUE2)) {
				event.getTarget().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 2, false, true));
				event.getPlayerPatch().getOriginal().addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 60, 4, false, true));
				event.getPlayerPatch().getOriginal().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 4, false, true));
			}
		});
	}
	@Override
	public void updateContainer(SkillContainer container) {
		super.updateContainer(container);
		AttributeInstance speed1 = container.getExecutor().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED);
		if (speed1 != null) {
			speed1.removeModifier(SPEED_MODIFIER_UUID);
			AttributeModifier modifier = new AttributeModifier(
					SPEED_MODIFIER_UUID,
					"speed1",
					container.getExecutor().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).getValue() * 0.4f,
					AttributeModifier.Operation.ADDITION
			);
			speed1.addTransientModifier(modifier);
		}
		if (container.getDataManager().getDataValue(WUSONG_SHEATH.get())) {
			AttributeInstance speed2 = container.getExecutor().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED);
			if (speed2 != null) {
				speed2.removeModifier(SPEED2_MODIFIER_UUID);
				AttributeModifier modifier = new AttributeModifier(
						SPEED2_MODIFIER_UUID,
						"speed2",
						container.getExecutor().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).getValue() * 0.2f,
						AttributeModifier.Operation.ADDITION
				);
				speed2.addTransientModifier(modifier);
			}
		} else {
			AttributeInstance speed2 = container.getExecutor().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED);
			if (speed2 != null) {
				speed2.removeModifier(SPEED2_MODIFIER_UUID);
			}
		}
		if (container.getExecutor().getOriginal() instanceof ServerPlayer serverPlayer) {
			if (container.getDataManager().getDataValue(CHECK3.get()) >= 0)
				container.getDataManager().setDataSync(CHECK3.get(), container.getDataManager().getDataValue(CHECK3.get()) - 1, serverPlayer);
			if (container.getDataManager().getDataValue(CHECK2.get()) >= 0)
				container.getDataManager().setDataSync(CHECK2.get(), container.getDataManager().getDataValue(CHECK2.get()) - 1, serverPlayer);
			if (container.getDataManager().getDataValue(CHECK1.get()) >= 0)
				container.getDataManager().setDataSync(CHECK1.get(), container.getDataManager().getDataValue(CHECK1.get()) - 1, serverPlayer);
		}

	}
	@Override
	public void onRemoved(SkillContainer container) {
		container.getExecutor().getEventListener().removeListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID);
		container.getExecutor().getEventListener().removeListener(EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID);
		container.getExecutor().getEventListener().removeListener(EventType.TAKE_DAMAGE_EVENT_DAMAGE, EVENT_UUID);
		container.getExecutor().getEventListener().removeListener(EventType.DEAL_DAMAGE_EVENT_ATTACK, EVENT_UUID);
		AttributeInstance speed1 = container.getExecutor().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED);
		if (speed1 != null) {
			speed1.removeModifier(SPEED_MODIFIER_UUID);
		}
		AttributeInstance speed2 = container.getExecutor().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED);
		if (speed2 != null) {
			speed2.removeModifier(SPEED2_MODIFIER_UUID);
		}
	}

	@Override
	public void onReset(SkillContainer container) {
		PlayerPatch<?> executer = container.getExecutor();
		if (!executer.isLogicalClient() && container.getDataManager().getDataValue(WUSONG_SHEATH.get())) {
			ServerPlayerPatch playerpatch = (ServerPlayerPatch)executer;
			container.getDataManager().setDataSync(WUSONG_SHEATH.get(), false, playerpatch.getOriginal());
			playerpatch.modifyLivingMotionByCurrentItem();
			container.getSkill().setConsumptionSynchronize(playerpatch.getSkill(SkillSlots.WEAPON_INNATE), 0.0F);
		}

	}

	@Override
	public void setConsumption(SkillContainer container, float value) {
		if (!container.getExecutor().isLogicalClient()) {
			if (container.getMaxResource() < value) {
				container.getDataManager().setDataSync(WUSONG_SHEATH.get(), true);
				container.getServerExecutor().modifyLivingMotionByCurrentItem(false);
				container.getServerExecutor().playAnimationInClientSide(Animations.BIPED_UCHIGATANA_SCRAP, 0.0F);
			}
		}

		super.setConsumption(container, value);
	}

	@Override
	public boolean shouldDeactivateAutomatically(PlayerPatch<?> executer) {
		return true;
	}
	
	@Override
	public float getCooldownRegenPerSecond(PlayerPatch<?> player) {
		if (player.getSkill(this).getDataManager().getDataValue(WUSONG_SHEATH.get())) {
			return 0.0F;
		}
		return player.getOriginal().isUsingItem() ? 0.0F : 1.0F;
	}
}