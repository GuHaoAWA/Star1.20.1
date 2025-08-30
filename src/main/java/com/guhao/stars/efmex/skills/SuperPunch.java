/*
package com.guhao.stars.efmex.skills;

import com.guhao.stars.efmex.StarAnimations;
import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.network.server.SPPlayAnimationInstant;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;


public class SuperPunch extends WeaponInnateSkill {

    public SuperPunch(Builder<? extends Skill> builder) {
		super(builder);

	}

	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {

		EntityState playerState = executer.getEntityState();
		if ((playerState.canUseSkill() && playerState.getLevel() != 1 || playerState.canUseSkill() && playerState.getLevel() != 2 || playerState.getLevel() != 3)) {
			executer.playAnimationSynchronized(StarAnimations.OLA, 0.16f);
		}
	}
	@Override
	public boolean checkExecuteCondition(PlayerPatch<?> executer) {
		EntityState playerState = executer.getEntityState();
		return (playerState.canUseSkill() && playerState.getLevel() != 1 || playerState.canUseSkill() && playerState.getLevel() != 2 || playerState.getLevel() != 3);
	}
}*/
