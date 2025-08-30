/*
package com.guhao.stars.efmex.skills;

import com.google.common.collect.Maps;

import com.guhao.stars.efmex.StarAnimations;
import com.guhao.stars.efmex.StarSkillDataKeys;
import io.redspace.ironsspellbooks.entity.spells.black_hole.BlackHole;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.corruptdog.cdm.api.animation.types.KnockbackAnimation;
import net.corruptdog.cdm.gameasset.CorruptAnimations;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

import java.util.Map;
import java.util.UUID;

import static com.guhao.stars.efmex.StarSkillDataKeys.CHECK2;
import static com.guhao.stars.efmex.StarSkillDataKeys.WUSONG_SHEATH;

public class WuSongSkill extends WeaponInnateSkill {


	public final Map<StaticAnimation, AttackAnimation> comboAnimation = Maps.newHashMap();
	public final Map<StaticAnimation, AttackAnimation> ex_comboAnimation = Maps.newHashMap();
	private static final UUID EVENT_UUID = UUID.fromString("d706b5bc-b98b-cc49-b83e-16ae590db349");

	public WuSongSkill(Builder<? extends Skill> builder) {

		super(builder);
	}
	@Override
	public void onInitiate(SkillContainer container) {

	}

	@Override
	public void onRemoved(SkillContainer container) {
		super.onRemoved(container);
	}

	@Override
	public WeaponInnateSkill registerPropertiesToAnimation() {
		this.comboAnimation.clear();
		this.ex_comboAnimation.clear();
		this.comboAnimation.put(Animations.TACHI_AUTO1, (AttackAnimation) Animations.RUSHING_TEMPO1);
		this.comboAnimation.put(Animations.TACHI_AUTO2, (AttackAnimation) Animations.RUSHING_TEMPO2);
		this.comboAnimation.put(Animations.TACHI_AUTO3, (AttackAnimation) Animations.RUSHING_TEMPO3);
		this.comboAnimation.put(Animations.RUSHING_TEMPO1, (AttackAnimation) CorruptAnimations.YAMATO_AUTO1);
		this.comboAnimation.put(Animations.RUSHING_TEMPO2, (AttackAnimation) CorruptAnimations.YAMATO_AUTO1);
		this.comboAnimation.put(Animations.RUSHING_TEMPO3, (AttackAnimation) CorruptAnimations.YAMATO_AUTO1);

		this.ex_comboAnimation.put(Animations.TACHI_AUTO3, (AttackAnimation) CorruptAnimations.LONGSWORD_OLD_AUTO1);
		this.ex_comboAnimation.put(CorruptAnimations.LONGSWORD_OLD_AUTO1, (AttackAnimation) CorruptAnimations.TACHI_TWOHAND_AUTO_4);
		this.ex_comboAnimation.put(CorruptAnimations.TACHI_TWOHAND_AUTO_4, (AttackAnimation) CorruptAnimations.TACHI_TWOHAND_AUTO_2);
		this.ex_comboAnimation.put(CorruptAnimations.TACHI_TWOHAND_AUTO_2, (AttackAnimation) CorruptAnimations.LONGSWORD_OLD_AUTO3);
		this.ex_comboAnimation.put(CorruptAnimations.LONGSWORD_OLD_AUTO3, (AttackAnimation) WOMAnimations.AGONY_AUTO_2);
		this.ex_comboAnimation.put(WOMAnimations.AGONY_AUTO_2, (AttackAnimation) WOMAnimations.AGONY_AUTO_1.addEvents(AnimationEvent.TimeStampedEvent.create(0.2F, (ep, anim, objs) -> {
			ep.playSound(EpicFightSounds.SWORD_IN.get(),1.0f,1.0f);
			if (ep instanceof ServerPlayerPatch serverPlayerPatch){
				serverPlayerPatch.getSkill(this).getDataManager().setDataSync(WUSONG_SHEATH.get(),true,serverPlayerPatch.getOriginal());
			}
			ep.getOriginal().addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 60, 9, false, true));
		}, AnimationEvent.Side.SERVER)));

//		this.comboAnimation.values().forEach((animation) -> animation.phases[0].addProperties((this.properties.get(0)).entrySet()));
		return this;
	}

	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		boolean isSheathed = executer.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(WUSONG_SHEATH.get());
		Skill skill = executer.getSkill(this).getSkill();
		KnockbackAnimation heidong = (KnockbackAnimation) CorruptAnimations.YAMATO_POWER1.get();
		heidong.addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(3.0f))
				.addEvents(AnimationEvent.TimeStampedEvent.create(3.0f, (ep, anim, objs) -> {
					float radius = 48F;

					Vec3 center = new Vec3(executer.getOriginal().getX(),executer.getOriginal().getEyeY(),executer.getOriginal().getZ());
					executer.getOriginal().level().playSound(null, center.x, center.y, center.z, SoundRegistry.BLACK_HOLE_CAST.get(), SoundSource.AMBIENT, 4.0F, 1.0F);
					BlackHole blackHole = new BlackHole(executer.getOriginal().level(), executer.getOriginal());
					blackHole.setRadius(radius);
					blackHole.setDamage(10);
					blackHole.moveTo(center);
					executer.getOriginal().level().addFreshEntity(blackHole);
				}, AnimationEvent.Side.SERVER));
		if (this.ex_comboAnimation.containsKey(executer.getAnimator().getPlayerFor(null).getAnimation())) {
			executer.playAnimationSynchronized(this.ex_comboAnimation.get(executer.getAnimator().getPlayerFor(null).getAnimation()), 0.0F);
			super.executeOnServer(executer, args);
			return;
		}
		if (executer.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(CHECK2.get()) > 0) {
			executer.playAnimationSynchronized(StarAnimations.AOXUE2.getAccessor(), 0.0F);
			executer.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().setDataSync(CHECK2.get(),0,executer.getOriginal());
			super.executeOnServer(executer.getSkill(SkillSlots.WEAPON_INNATE), args);
			return;
		}
		if (executer.getSkill(SkillSlots.WEAPON_INNATE).getStack() == 11 && isSheathed && executer.getOriginal().isSprinting()) {
			executer.playAnimationSynchronized(heidong, 0.0F);
			skill.setStackSynchronize(executer.getSkill(SkillSlots.WEAPON_INNATE),0);
			super.executeOnServer(executer.getSkill(SkillSlots.WEAPON_INNATE), args);
			return;
		}
		if (executer.getSkill(SkillSlots.WEAPON_INNATE).getStack() == 9 && !isSheathed && executer.getOriginal().isSprinting()) {
			AttackAnimation c1 = (AttackAnimation) Animations.TACHI_AUTO3;
			executer.playAnimationSynchronized(c1, 0.0F);
			super.executeOnServer(executer.getSkill(SkillSlots.WEAPON_INNATE), args);
			return;
		}
		if (executer.getSkill(SkillSlots.WEAPON_INNATE).getStack() == 5 && !executer.getOriginal().onGround) {
			executer.playAnimationSynchronized(StarAnimations.AOXUE.getAccessor(), 0.0F);
			skill.setStackSynchronize(executer.getSkill(SkillSlots.WEAPON_INNATE),2);
			super.executeOnServer(executer.getSkill(SkillSlots.WEAPON_INNATE), args);
			return;
		}
		if (executer.getSkill(SkillSlots.WEAPON_INNATE).getStack() == 2) {
			executer.playAnimationSynchronized(StarAnimations.HANGDANG.getAccessor(), 0.0F);
			super.executeOnServer(executer.getSkill(SkillSlots.WEAPON_INNATE), args);
			return;
		}
		if (executer.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(StarSkillDataKeys.CHECK3.get()) > 0) {
			executer.playAnimationSynchronized(WOMAnimations.AGONY_AUTO_2, 0.0F);
			executer.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().setDataSync(StarSkillDataKeys.CHECK3.get(),0,executer.getOriginal());
			super.executeOnServer(executer.getSkill(SkillSlots.WEAPON_INNATE), args);
			return;
		}
		if (this.comboAnimation.containsKey(executer.getAnimator().getPlayerFor(null).getAnimation().get())) {
			executer.playAnimationSynchronized(this.comboAnimation.get(executer.getAnimator().getPlayerFor(null).getAnimation().get()), 0.0F);
			super.executeOnServer(executer.getSkill(SkillSlots.WEAPON_INNATE), args);
			return;
		}
		if (isSheathed) {
			executer.playAnimationSynchronized(Animations.BATTOJUTSU_DASH, -0.694F);
			super.executeOnServer(executer.getSkill(SkillSlots.WEAPON_INNATE), args);
        } else {
			executer.playAnimationSynchronized(Animations.BATTOJUTSU_DASH, 0.0F);
			super.executeOnServer(executer.getSkill(SkillSlots.WEAPON_INNATE), args);
        }
    }
}*/
