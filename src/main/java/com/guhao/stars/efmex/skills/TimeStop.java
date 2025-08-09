package com.guhao.stars.efmex.skills;

import com.guhao.stars.efmex.StarAnimations;
import com.guhao.stars.regirster.Sounds;
import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.network.server.SPMoveAndPlayAnimation;
import yesman.epicfight.network.server.SPPlayAnimation;
import yesman.epicfight.network.server.SPPlayAnimationInstant;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;


public class TimeStop extends WeaponInnateSkill {

    public TimeStop(Builder<? extends Skill> builder) {
		super(builder);

	}

	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		if (executer.getOriginal().isShiftKeyDown() && executer.getSkill(this).getStack() >= 2) {
			executer.getSkill(this).getSkill().setStackSynchronize(executer,0);
			executer.playAnimationSynchronized(StarAnimations.THE_WORLD,0.0f);
			return;
		}
		EntityState playerState = executer.getEntityState();
		if ((playerState.canUseSkill() && playerState.getLevel() != 1 || playerState.canUseSkill() && playerState.getLevel() != 2 || playerState.getLevel() != 3) || !playerState.inaction()) {
			executer.playSound(Sounds.MUDA.get(),1.2f ,1.0f,1.0f);
			executer.playAnimationSynchronized(StarAnimations.OLA, 0.12f);
		}
	}
	@Override
	public boolean checkExecuteCondition(PlayerPatch<?> executer) {
		EntityState playerState = executer.getEntityState();
		return (playerState.canUseSkill() && playerState.getLevel() != 1 || playerState.canUseSkill() && playerState.getLevel() != 2 || playerState.getLevel() != 3 || !playerState.inaction());
	}
}