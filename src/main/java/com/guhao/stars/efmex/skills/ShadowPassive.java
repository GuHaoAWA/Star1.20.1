package com.guhao.stars.efmex.skills;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;

public class ShadowPassive extends Skill {
    private static final UUID EVENT_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174009");
    public ShadowPassive(Builder<? extends Skill> builder) {
        super(builder);
    }
    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID, (event) -> {
            if (event.getDamageSource().getEntity() instanceof LivingEntity target) {
                container.getExecuter().getOriginal().hurtMarked = true;
                Vec3 viewVec = target.getViewVector(1.0F);
                container.getExecuter().getOriginal().teleportTo(target.getX() + viewVec.x() * 4.5f, target.getY(), target.getZ() + viewVec.z() * 4.5f);
//                Vec3 newTargetPos = target.position();
//                container.getExecuter().getOriginal().lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(newTargetPos.x, newTargetPos.y + target.getEyeHeight(), newTargetPos.z));
                container.getExecuter().playAnimationSynchronized(Animations.BATTOJUTSU_DASH,-0.60F);
                container.getExecuter().playSound(EpicFightSounds.EVISCERATE.get(),0f,0f);
            }
        },-1);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID, (event) -> {
            if (container.getExecuter().getAnimator().getPlayerFor(null).getAnimation() == Animations.BATTOJUTSU_DASH) {
                event.setResult(AttackResult.ResultType.MISSED);
                event.setCanceled(true);
            }
        },-1);
    }
    @Override
    public void onRemoved(SkillContainer container) {
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID);
    }
}
